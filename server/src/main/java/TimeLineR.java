package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import net.ncaq.chat.sd.util.*;

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

                final ExecutorService g = Executors.newSingleThreadExecutor();
                g.execute(getTimeLineR());

                final ExecutorService p = Executors.newSingleThreadExecutor();
                p.execute(postTimeLineR());

                try {
                    while(g.awaitTermination(1, TimeUnit.HOURS) && p.awaitTermination(1, TimeUnit.HOURS)) {
                        Thread.sleep(1);
                    }
                }
                catch(final InterruptedException err) {
                    System.err.println(err);
                }
            }
        }
        catch(final IllegalStateException err) {
            get.println(new StatusCode(100).toString());
        }
    }

    /**
     * 新規メッセージの配信を準備します
     */
    public void put(final String newMessage) throws InterruptedException {
        this.messageBox.put(newMessage);
    }

    private final Runnable getTimeLineR() {
        return () -> {
            for(;;) {
                try {
                    get.println(messageBox.take());
                }
                catch(NullPointerException|InterruptedException err) {
                    System.err.println(err);
                    break;
                }
            };
        };
    }

    private final Runnable postTimeLineR() {
        return () -> {
            for(;;) {
                try {
                    server.broadcast("chat " + user.getUsername() + " " + post.readLine());
                }
                catch(NullPointerException|IOException err) {
                    System.err.println(err);
                    break;
                }
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
