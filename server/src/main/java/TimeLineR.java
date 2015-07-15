package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import lombok.*;
import net.ncaq.chat.sd.util.*;
import net.ncaq.chat.sd.util.message.*;
import static java.util.concurrent.TimeUnit.*;

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
            val loginMessage = auth.login(user);
            val loginMessage = new LoginMessage(user, "login");
            System.err.println();

            get.println(loginMessage.toString());

            if(loginMessage.equals(LOGIN_SUCCEED)) {
                System.err.println(loginMessage);
                server.broadcast(loginMessage);

                try {
                    final ExecutorService getThread = Executors.newSingleThreadExecutor();
                    getThread.execute(getTimeLineR()); // getは非同期実行
                    postTimeLineR().run();     // postは｢同期｣実行
                    getThread.shutdown();      // getは自動で終了しない
                }
                finally {
                    auth.logout(user); // 確実にログアウト
                    System.err.println(new Message(user, MessageType.CONTROL, "さんが終了しました"));
                }
            }
        }
        catch(final IllegalStateException err) { // 正規表現例外などに対処
            get.println(PASSWORD_INVALID.toString());
        }
        finally {               // 早期開放のため明示的に開放
            try {
                get.close();
                post.close();
                client.close();
            }
            catch(final IOException err) { // もし例外が出ても,開放が遅れるだけで自動開放はされるはず
                System.err.println(err);
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
            this.messageBox.put(newMessage);
        }
        catch(final InterruptedException err) {
            System.err.println(err);
        }
    }

    /**
     * これは終了しない.
     * 強制終了すること.
     */
    private final Runnable getTimeLineR() {
        return () -> {
            try {
                for(String m = messageBox.take(); ; m = messageBox.take()) {
                    if(m != null) {
                        get.println(m);
                    }
                }
            }
            catch(final InterruptedException err) {
                System.err.println(err);
            }
        };
    }

    private final Runnable postTimeLineR() {
        return () -> {
            try {
                for(String l = post.readLine(); l != null; l = post.readLine()) {
                    server.broadcast(new Message(user, MessageType.CHAT, l));
                }
            }
            catch(final IOException err) {
                System.err.println(err);
            }
        };
    }

    private final LinkedBlockingQueue<String> messageBox = new LinkedBlockingQueue<>();

    private final CentralServer server;
    private final Socket client;
    private final Auth auth;

    private final PrintWriter get;
    private final BufferedReader post;
    private final User user;
}
