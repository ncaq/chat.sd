package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import lombok.*;
import net.ncaq.chat.sd.*;
import net.ncaq.chat.sd.server.message.*;
import static java.util.concurrent.TimeUnit.*;
import static net.ncaq.chat.sd.Status.*;

@ToString
public class TimeLineR implements Runnable {
    public TimeLineR(final CentralServer server, final Socket client, final Auth auth) throws IOException {
        this.server = server;
        this.client = client;
        this.auth = auth;
        this.get = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream())), true);
        this.post = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.user = new User(post.readLine());
    }

    public void run() {
        try {
            val loginedUser = auth.loginedUsersInfo(); //ログインする前に,ログイン中ユーザ情報を取得.

            val loginStatus = auth.login(this.user);
            get.println(loginStatus.toString());
            System.out.println(loginStatus.toString());

            if(loginStatus == LOGIN_SUCCEED) {
                server.chatLog(10).stream().map(ChatMessage::toOldChat).forEach(this::put);
                this.put(loginedUser);

                val lm = new LoginMessage();
                lm.setPoster(this.user);
                server.broadcast(lm);

                server.addReadiedSession(this);

                try {
                    final ExecutorService getThread = Executors.newSingleThreadExecutor();
                    getThread.execute(this::getTimeLineR); // getは非同期実行
                    postTimeLineR();     // postは｢同期｣実行
                    getThread.shutdown();      // getは自動で終了しない
                }
                finally {
                    auth.logout(user); // 確実にログアウト
                    val m = new LogoutMessage();
                    m.setPoster(this.user);
                    server.broadcast(m);
                }
            }
        }
        catch(final Exception exc) { // 正規表現例外などに対処
            System.err.println(exc);
            get.println(PASSWORD_INVALID.getDescription());
        }
        finally {               // 早期開放のため明示的に開放
            try {
                get.close();
                post.close();
                client.close();
            }
            catch(final IOException exc) { // もし例外が出ても,開放が遅れるだけで自動開放はされるはず
                System.err.println(exc);
            }
            finally {
                server.removeClosedSession(this); // サーバにセッションの終了を通知
            }
        }
    }

    /**
     * 新規メッセージの配信を準備します.
     */
    public void put(final String newMessage) {
        try {
            this.newMessageTextBox.put(newMessage);
        }
        catch(final InterruptedException exc) {
            System.err.println(exc);
        }
    }

    /**
     * PrintWriterはスレッドセーフではないため,一度ブロッキングキューを経由します.
     * これは終了しない.
     * 強制終了すること.
     */
    private void getTimeLineR() {
        try {
            for(String m = newMessageTextBox.take(); m != null; m = newMessageTextBox.take()) {
                get.println(m);
            }
        }
        catch(final InterruptedException exc) {
            System.err.println(exc);
        }
    }

    private void postTimeLineR() {
        try {
            for(String l = post.readLine(); l != null; l = post.readLine()) {
                ChatMessage m = new ChatMessage();
                m.setPoster(user);
                m.setBody(l);
                server.broadcast(m);
            }
        }
        catch(final IOException exc) {
            System.err.println(exc);
        }
    }

    private final LinkedBlockingQueue<String> newMessageTextBox = new LinkedBlockingQueue<>();

    private final CentralServer server;
    private final Socket client;
    private final Auth auth;

    private final PrintWriter get;
    private final BufferedReader post;

    @Getter
    private final User user;
}
