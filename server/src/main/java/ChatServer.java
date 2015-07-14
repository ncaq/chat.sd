package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    public ChatServer(final Integer port) {
        try {
            final ServerSocket socket = new ServerSocket(port);

            new Thread(() -> {
                    for(;;) {
                        try {
                            final TimeLineR newSession = new TimeLineR(this, socket.accept(), auth);
                            final ExecutorService thread = Executors.newCachedThreadPool();
                            thread.execute(newSession);
                            sessions.put(newSession);
                        }
                        catch(IOException|InterruptedException err) {
                            System.err.println(err);
                        }
                    }}).start();

            new Thread(() -> {
                    for(;;) {
                        try {
                            final String nm = newMessageBox.take();
                            sessions.parallelStream().forEach(s -> {
                                    try {
                                        s.put(nm);
                                    }
                                    catch(final InterruptedException err) {
                                        System.err.println(err);
                                    }});
                        }
                        catch(final InterruptedException err) {
                            System.err.println(err);
                        }}}).start();
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

    private final LinkedBlockingQueue<TimeLineR> sessions = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<String> newMessageBox = new LinkedBlockingQueue<>();
    private final Auth auth = new Auth();
    private final Log log = Log.getInstance();
}
