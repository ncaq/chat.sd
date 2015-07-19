package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.inject.*;
import javax.persistence.*;
import lombok.*;
import net.ncaq.chat.sd.message.*;

public class CentralServer {
    public CentralServer(final Integer port) {
        try {
            final ServerSocket socket = new ServerSocket(port);
            System.out.println("create socket");

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
        }
        catch(final IOException err) {
            System.err.println(err);
        }
    }

    /**
     * 全てのセッションに新規メッセージを配信します.
     */
    public void broadcast(final Message newMessage) {
        pool.execute(() -> {
                final String messageForTimeLine = newMessage.forTimeLine();
                sessions.parallelStream().forEach(s -> s.put(messageForTimeLine));

                final EntityTransaction tr = em.getTransaction();
                tr.begin();
                em.persist(newMessage);
                tr.commit();
                System.out.println(newMessage);
            });
    }

    /**
     * 閉じたセッションを配信から排除します.
     */
    public void removeClosedSession(final TimeLineR closedSession) {
        pool.execute(() -> {
                sessions.remove(closedSession);
                System.out.println("close session: " + closedSession);
            });
    }

    private final ExecutorService pool = Executors.newCachedThreadPool();
    private final Queue<TimeLineR> sessions = new ConcurrentLinkedQueue<>();
    private final Auth auth = new Auth();

    private final EntityManager em = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence").createEntityManager();
}
