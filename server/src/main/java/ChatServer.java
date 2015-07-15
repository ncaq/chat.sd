package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    public ChatServer(final Integer port) {
        try {
            final ServerSocket socket = new ServerSocket(port);
            System.err.println("create socket");

            pool.execute(() -> {
                    for(;;) {
                        try {
                            final TimeLineR newSession = new TimeLineR(this, socket.accept(), auth);
                            pool.execute(newSession);
                            sessions.put(newSession);
                        }
                        catch(IOException|InterruptedException err) {
                            System.err.println(err);
                        }
                    }
                });
            pool.execute(() -> {
                    for(;;) {
                        try {
                            final String nm = newMessageBox.take();
                            sessions.parallelStream().forEach(s -> s.put(nm));
                        }
                        catch(final InterruptedException err) {
                            System.err.println(err);
                        }
                    }
                });
        }
        catch(final IOException err) {
            System.err.println(err);
        }
    }

    public void broadcast(final String newMessageBox) {
        try {
            this.newMessageBox.put(newMessageBox);
            this.log.write(newMessageBox);
        }
        catch(final InterruptedException err) {
            System.err.println(err);
        }
    }

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final Queue<TimeLineR> sessions = new ConcurrentLinkedQueue<>();
    private final Queue<String> newMessageBox = new LinkedBlockingQueue<>();
    private final Auth auth = new Auth();
    private final Log log = Log.getInstance();
}
