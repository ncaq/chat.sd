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
    public Auth() {
        this.addUser(new User("anonymous", ""));
        this.addUser(new User("guest0", "0"));
        this.addUser(new User("guest1", "1"));
        this.addUser(new User("guest2", "2"));
        this.addUser(new User("guest3", "3"));
    }

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
     * ユーザーを追加.
     * 既に完全に一致するユーザが追加されているときは何も行わない.
     */
    public void addUser(final User u) {
        if(!this.correctUser(u)) { // 存在しない時のみ追加
            val tr = this.em.getTransaction();
            tr.begin();
            this.em.persist(u);
            tr.commit();
        }
    }

    /**
     * 正しいパスワードのユーザがデータベースに存在するか.
     * @param u 検索対象ユーザ.
     * @return 存在する時 {@code true}.
     * TODO 現在の実装方法は不適切.
     * inを使う形式で書き換えるべき.
     */
    public boolean correctUser(final User u) {
        val cbuilder = this.em.getCriteriaBuilder();
        val q = cbuilder.createQuery(User.class);
        val userRoot = q.from(User.class);
        q.select(userRoot).where(cbuilder.equal(userRoot, u));
        return this.em.createQuery(q).getResultList().size() == 1;
    }

    private final Set<User> logined = new ConcurrentSkipListSet<>();

    private final EntityManager em = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence").createEntityManager();
}
