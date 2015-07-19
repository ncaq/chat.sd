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

            threadPool.execute(() -> {
                    for(;;) {
                        try {
                            threadPool.execute(new TimeLineR(this, socket.accept(), auth));
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
        threadPool.execute(() -> {
                final String toTimeLine = newMessage.toTimeLine();
                sessions.parallelStream().forEach(s -> s.put(toTimeLine));

                final EntityTransaction tr = em.getTransaction();
                tr.begin();
                em.persist(newMessage);
                tr.commit();
                System.out.println(newMessage);
            });
    }

    /**
     * 最近のチャットメッセージを10件送信し,配信準備が完了したセッションをメッセージの配信対象にします.
     */
    public void addReadiedSession(final TimeLineR readiedSession) {
        val cb = this.em.getCriteriaBuilder();
        val q = cb.createQuery(ChatMessage.class);
        val root = q.from(ChatMessage.class);
        q.select(root).orderBy(cb.desc(root.get(ChatMessage_.submit)));
        val messages = this.em.createQuery(q).setMaxResults(10).getResultList();
        Collections.reverse(messages); // 新しい順 -> 古い順

        messages.stream().map(ChatMessage::toOldChat).forEach(readiedSession::put);

        sessions.add(readiedSession);

        val lm = new LoginMessage();
        lm.setPoster(readiedSession.getUser());
        this.broadcast(lm);
    }

    /**
     * 閉じたセッションを配信から排除します.
     */
    public void removeClosedSession(final TimeLineR closedSession) {
        threadPool.execute(() -> {
                sessions.remove(closedSession);
                System.out.println("close session: " + closedSession);
            });
    }

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Queue<TimeLineR> sessions = new ConcurrentLinkedQueue<>();
    private final Auth auth = new Auth();

    private final EntityManager em = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence").createEntityManager();
}
