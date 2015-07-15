package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import net.ncaq.chat.sd.server.message.*;

public class CentralServer {
    public CentralServer(final Integer port) {
        try {
            final ServerSocket socket = new ServerSocket(port);
            System.err.println("create socket");

            pool.execute(() -> {
                    for(;;) {
                        try {
                            final TimeLineR newSession = new TimeLineR(this, socket.accept(), auth);
                            pool.execute(newSession);
                            sessions.add(newSession);
                        }
                        catch(final IOException err) {
                            System.err.println(err);
                        }
                    }
                });
            pool.execute(() -> {
                    for(;;) {
                        try {
                            final String nm = newMessageBox.take();
                            pool.execute(() -> sessions.parallelStream().forEach(s -> s.put(nm)));
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

    /**
     * 全てのセッションに新規メッセージを配信する準備をします.
     */
    public void broadcast(final Message newMessage) {
        try {
            this.newMessageBox.put(newMessage);
        }
        catch(final InterruptedException err) {
            System.err.println(err);
        }
    }

    /**
     * 閉じたセッションを配信から排除します.
     */
    public void removeClosedSession(final TimeLineR closedSession) {
        pool.execute(() -> {
                sessions.remove(closedSession);
                System.err.println("close session: " + closedSession);
            });
    }

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final Queue<TimeLineR> sessions = new ConcurrentLinkedQueue<>();
    private final BlockingQueue<Message> newMessageBox = new LinkedBlockingQueue<>();
    private final Auth auth = new Auth();
}
