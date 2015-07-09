package net.ncaq.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class SessionHandler extends Thread {
    public SessionHandler(final ChatServer server, final Socket client) throws IOException {
        this.server = server;
        this.client = client;

        this.reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream())), true);
        this.receiveTimeLine = new LinkedBlockingQueue<>();
    }

    public void run() {
        final ExecutorService e = Executors.newSingleThreadExecutor();
        e.execute(() -> {
                for(;;) {
                    try {
                        this.writer.println(this.receiveTimeLine.take());
                    }
                    catch(final InterruptedException err) {
                        System.err.println(err);
                    }}});

        while(this.client.isConnected()) {
            try {
                final String l = this.reader.readLine();
                if(l == null) {
                    break;
                }
                else {
                    this.server.broadcast(l);
                }
            }
            catch(final IOException err) {
                System.err.println(err);
            }
            catch(final InterruptedException err) {
                System.err.println(err);
            }};

        e.shutdown();
    }

    public void put(final String newMessage) throws InterruptedException {
        this.receiveTimeLine.put(newMessage);
    }

    private final ChatServer server;
    private final Socket client;

    private final BufferedReader reader;
    private final PrintWriter writer;
    private final LinkedBlockingQueue<String> receiveTimeLine;
}
