package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import net.ncaq.chat.sd.util.*;
import static java.util.concurrent.TimeUnit.*;

public class TimeLineR implements Runnable {
    public TimeLineR(final ChatServer server, final Socket client, final Auth auth) throws IOException {
        this.server = server;
        this.client = client;
        this.auth = auth;
        this.get = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream())), true);
        this.post = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.user = new User(post.readLine());
    }

    public void run() {
        try {
            final StatusCode loginStatus = auth.login(user);
            System.err.println(user.getUsername() + " is " + loginStatus.toString());

            get.println(loginStatus.toString());

            if(loginStatus.equals(new StatusCode(0))) { // ログイン成功
                final String loginMessage = "login " + user.getUsername();
                System.err.println(loginMessage);
                server.broadcast(loginMessage);

                try {
                    final ExecutorService e = Executors.newSingleThreadExecutor();
                    e.execute(getTimeLineR());
                    postTimeLineR().run(); // postが終了したらgetを強制終了する
                    e.shutdown();
                }
                finally {       // 確実にログアウト
                    auth.logout(user);
                    System.err.println(user.getUsername() + "さんが終了しました");
                }
            }
        }
        catch(final IllegalStateException err) { // 正規表現例外などに対処
            get.println(new StatusCode(100).toString());
        }
    }

    /**
     * 新規メッセージの配信を準備します.
     */
    public void put(final String newMessage) throws InterruptedException {
        this.messageBox.put(newMessage);
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
                    server.broadcast("chat " + user.getUsername() + " " + l);
                }
            }
            catch(final IOException err) {
                System.err.println(err);
            }
        };
    }

    private final LinkedBlockingQueue<String> messageBox = new LinkedBlockingQueue<>();

    private final ChatServer server;
    private final Socket client;
    private final Auth auth;

    private final PrintWriter get;
    private final BufferedReader post;
    private final User user;
}
