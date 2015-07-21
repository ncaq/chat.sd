package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import javax.inject.*;
import javax.persistence.*;
import lombok.*;
import net.ncaq.chat.sd.server.message.*;

public class CentralServer {
    public CentralServer(final Integer port) {
        try {
            final ServerSocket socket = new ServerSocket(port);
            System.out.println("create socket");

            threadPool.execute(() -> {
                    for(;;) {
                        try {
                            threadPool.execute(new Session(this, socket.accept(), auth));
                        }
                        catch(final Exception err) {
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
    public Future<?> broadcast(final Message newMessage) {
        return threadPool.submit(() -> {
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
     * 配信準備が完了したセッションをメッセージの配信対象にします.
     */
    public void addReadiedSession(final Session readiedSession) {
        sessions.add(readiedSession);
    }

    /**
     * 閉じたセッションを配信から排除します.
     */
    public void removeClosedSession(final Session closedSession) {
        threadPool.execute(() -> {
                sessions.remove(closedSession);
                System.out.println("close session: " + closedSession);
            });
    }

    /**
     * 最近のチャットログを返します.
     */
    public List<OldChatMessage> chatLog(final Integer limit) {
        val cb = this.em.getCriteriaBuilder();
        val q = cb.createQuery(ChatMessage.class);
        val root = q.from(ChatMessage.class);
        q.select(root).orderBy(cb.desc(root.get(ChatMessage_.submit)));
        val messages = this.em.createQuery(q).setMaxResults(limit).getResultList();
        Collections.reverse(messages); // 新しい順 -> 古い順
        return messages.stream().map(m -> OldChatMessage.of(m)).collect(Collectors.toList());
    }

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    private final Auth auth = new Auth();

    private final EntityManager em = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence").createEntityManager();
}
