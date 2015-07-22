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

/**
 * 各ユーザのセッション.
 * CentralServerからメッセージ文字列を受け取って送信する.
 * ユーザのメッセージをCentralServerに送信する.
 */
@ToString
public class Session implements Runnable {
    public Session(final CentralServer server, final Socket client, final Auth auth) {
        this.server = server;
        this.client = client;
        this.auth = auth;
    }

    public void run() {
        try {
            this.get = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream())), true);
            this.post = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.user = new User(post.readLine());

            val loginedUser = auth.loginedUsersInfo(); //ログインする前に,ログイン中ユーザ情報を取得.

            val loginStatus = auth.login(this.user);
            get.println(loginStatus.toString());
            System.out.println(loginStatus.toString());

            if(loginStatus == LOGIN_SUCCEED) {
                server.chatLog(10).stream().map(ChatMessage::toOldTimeLine).forEach(get::println);
                get.println(loginedUser);

                val lm = new LoginMessage();
                lm.setPoster(this.user);
                server.broadcast(lm);

                try {
                    final ExecutorService getThread = Executors.newSingleThreadExecutor();
                    getThread.execute(this::getTimeLineR); // getは非同期実行
                    this.postTimeLineR();                  // postは｢同期｣実行
                    getThread.shutdown();                  // getは自動で終了しない
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
            exc.printStackTrace();
            get.println(PASSWORD_INVALID.getDescription());
        }
        finally {
            server.removeClosedSession(this); // サーバにセッションの終了を通知
        }
    }

    /**
     * 新規メッセージの配信を準備します.
     * PrintWriterはスレッドセーフではないため,一度ブロッキングキューを経由します.
     * そのため,即座には配信されない可能性があります.
     */
    public void put(final String newMessage) {
        try {
            this.newMessageBox.put(newMessage);
        }
        catch(final InterruptedException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * PrintWriterはスレッドセーフではないため,一度ブロッキングキューを経由します.
     * これは通常は終了しません.
     * 強制終了すること.
     */
    private void getTimeLineR() {
        try {
            for(String m = newMessageBox.take(); m != null; m = newMessageBox.take()) {
                get.println(m);
            }
        }
        catch(final InterruptedException exc) {
            exc.printStackTrace();
        }
    }

    /**
     * CentralServerにユーザのメッセージを送信します.
     */
    private void postTimeLineR() {
        try {
            for(String l = post.readLine(); l != null; l = post.readLine()) {
                val m = new ChatMessage();
                m.setPoster(user);
                m.setBody(l);
                server.broadcast(m);
            }
        }
        catch(final IOException exc) {
            exc.printStackTrace();
        }
    }

    private final LinkedBlockingQueue<String> newMessageBox = new LinkedBlockingQueue<>();

    private final CentralServer server;
    private final Socket client;
    private final Auth auth;

    // init by run()
    private PrintWriter get;
    private BufferedReader post;
    private User user;
}
