package net.ncaq.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    public static void main(final String[] args) throws IOException {
        new ChatServer((args.length == 0) ? 50000 : Integer.parseInt(args[0]));
    }

    public ChatServer(final Integer port) {
        try {
            final ServerSocket server = new ServerSocket(port);

            new Thread(() -> {
                    for(;;) {
                        try {
                            final SessionHandler newSession = new SessionHandler(this, server.accept());
                            newSession.start();
                            sessions.put(newSession);
                        }
                        catch(final IOException err) {
                            System.err.println(err);
                        }
                        catch(final InterruptedException err) {
                            System.err.println(err);
                        }
                    }}).start();

            new Thread(() -> {
                    for(;;) {
                        try {
                            final String newMessage = receive.take();
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

            new Thread(() -> {
                    for(;;) {
                        sessions.removeIf(s -> !s.isAlive());
                        try {
                            Thread.sleep(1000 * 60); // 1 minutes
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
        this.receive.put(newMessage);
        this.log.write(newMessage);
    }

    private final LinkedBlockingQueue<SessionHandler> sessions = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<String> receive = new LinkedBlockingQueue<>();
    private final Log log = Log.getInstance();
}
