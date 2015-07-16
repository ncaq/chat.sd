package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.persistence.*;
import lombok.*;
import net.ncaq.chat.sd.*;
import net.ncaq.chat.sd.message.*;

/**
 * 存在しないユーザのログイン,多重ログインを防ぎます.
 * スレッドセーフ
 */
public class Auth {
    public Message login(final User u) {
        Message m = !this.correctUser(u) ?
            new PasswordInvalidMessage() :
            logined.contains(u) || !logined.add(u) ? // add
            new MultipleLoginMessage() :
            new LoginMessage();
        m.setPoster(u);
        return m;
    };

    public Message logout(final User u) {
        Message m = !this.correctUser(u) ?
            new PasswordInvalidMessage() :
            !this.logined.remove(u) ? // remove
            new MultipleLogoutMessage() :
            new LogoutMessage();
        m.setPoster(u);
        return m;
    }

    /**
     * ユーザーを追加
     * @todo
     */
    public void addUser(final User u) {
        this.em.persist(u);
    }

    /**
     * ユーザがデータベースに存在するか.
     * @return true 存在する
     * @todo
     */
    public boolean correctUser(final User u) {
        val cbuilder = this.em.getCriteriaBuilder();
        val q = cbuilder.createQuery(User.class);
        val userRoot = q.from(User.class);
        q.select(userRoot).where(cbuilder.equal(userRoot.get(User_.name), u.getName()),
                                 cbuilder.equal(userRoot.get(User_.password), u.getPassword()));
        return this.em.createQuery(q).getResultList().size() == 1;
    }

    private final Set<User> logined = new ConcurrentSkipListSet<>();

    @PersistenceUnit
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence");

    @PersistenceContext
    private EntityManager em = emf.createEntityManager();
}
