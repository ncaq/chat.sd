package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class SessionHandler extends Runnable {
    public SessionHandler(final ChatServer server, final Socket client, final Auth auth) throws IOException {
        this.server = server;
        this.client = client;
        this.auth = auth;
    }

    public void run() {
        final BufferedReader receive = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        final PrintWriter send = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream())), true);

        final StatusCode loginStatus = auth.login(receive.readLine());
        send.println(loginStatus.toString());

        if(loginStatus.equals(new StatusCode(0))) { // ログイン成功
            final ExecutorService g = Executors.newSingleThreadExecutor();
            g.execute(new GetTimeLineR(send, this.messageBox));
            final ExecutorService p = Executors.newSingleThreadExecutor();
            p.execute(new PostTimeLineR(receive, this.server));
            try {
                while(g.awaitTermnation(1, TimeUnit.HOURS) && p.awaitTermnation(1, TimeUnit.HOURS)) {
                }
            }
            catch(final InterruptedException err) {
                System.err.println(err);
            }
        }
        else {
        }
    }

    public void put(final String newMessage) throws InterruptedException {
        this.messageBox.put(newMessage);
    }

    private final ChatServer server;
    private final Socket client;
    private final Auth auth;

    private final LinkedBlockingQueue<String> messageBox = new LinkedBlockingQueue<>();
}
