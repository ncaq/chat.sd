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
                            final SessionHandler newSession = new SessionHandler(this, socket.accept(), auth);
                            newSession.start();
                            sessions.put(newSession);
                        }
                        catch(IOException|InterruptedException err) {
                            System.err.println(err);
                        }
                    }}).start();

            new Thread(() -> {
                    for(;;) {
                        try {
                            final String newMessage = newMessage.take();
                            sessions.parallelStream().forEach(s -> {
                                    try {
                                        s.put(newMessage);
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

    public void broadcast(final String newMessage) throws InterruptedException {
        this.newMessage.put(newMessage);
        this.log.write(newMessage);
    }

    private final LinkedBlockingQueue<SessionHandler> sessions = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<String> newMessage = new LinkedBlockingQueue<>();
    private final Auth auth = new Auth();
    private final Log log = Log.getInstance();
}
