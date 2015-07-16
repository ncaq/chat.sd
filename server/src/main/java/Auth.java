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
        // userPassword.put(u.getName(), u.getPassword().toString());
    }

    /**
     * ユーザがデータベースに存在するか.
     * @return true 存在する
     * @todo
     */
    public boolean correctUser(final User u) {
        // return u.getPassword().toString().equals(userPassword.get(u.getName()));
        val cbuilder = this.em.getCriteriaBuilder();
        val q = cbuilder.createQuery(Boolean.class);
        val userRoot = q.from(User.class);
        q.select(userRoot).where(cbuilder.equals(userRoot.get(User_.name), u.getName()),
                                 cbuilder.equals(userRoot.get(User_.password), u.getPassword()));
        return this.em.createQuery(q).getSingleResult();
    }

    private final Set<User> logined = new ConcurrentSkipListSet<>();

    @PersistenceContext(unitName = "net.ncaq.chat.sd.persistence")
    private EntityManager em;
}
