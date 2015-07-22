package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import javax.persistence.*;
import lombok.*;
import net.ncaq.chat.sd.*;
import static net.ncaq.chat.sd.Status.*;

/**
 * 存在しないユーザのログイン,多重ログインを防ぎます.
 * スレッドセーフ
 */
public class Auth {
    public Auth() {
        this.addUser(new User("anonymous", ""));

        this.addUser(new User("user0", "0"));
        this.addUser(new User("user1", "1"));
        this.addUser(new User("user2", "2"));
        this.addUser(new User("user3", "3"));
        this.addUser(new User("user4", "4"));
        this.addUser(new User("user5", "5"));
        this.addUser(new User("user6", "6"));
        this.addUser(new User("user7", "7"));
        this.addUser(new User("user8", "8"));
        this.addUser(new User("user9", "9"));

        this.addUser(new User("guest0", "0"));
        this.addUser(new User("guest1", "1"));
        this.addUser(new User("guest2", "2"));
        this.addUser(new User("guest3", "3"));
        this.addUser(new User("guest4", "4"));
        this.addUser(new User("guest5", "5"));
        this.addUser(new User("guest6", "6"));
        this.addUser(new User("guest7", "7"));
        this.addUser(new User("guest8", "8"));
        this.addUser(new User("guest9", "9"));
    }

    public Status login(final User u) {
        return !this.correctUser(u) ?
            PASSWORD_INVALID :
            logined.contains(u) || !logined.add(u) ? // add
            MULTIPLE_LOGIN :
            LOGIN_SUCCEED;
    };

    public Status logout(final User u) {
        return !this.correctUser(u) ?
            PASSWORD_INVALID :
            !this.logined.remove(u) ? // remove
            MULTIPLE_LOGOUT :
            LOGOUT_SUCCEED;
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
     */
    public boolean correctUser(final User u) {
        val cbuilder = this.em.getCriteriaBuilder();
        val q = cbuilder.createQuery(User.class);
        val userRoot = q.from(User.class);
        q.select(userRoot).where(cbuilder.equal(userRoot, u));
        return this.em.createQuery(q).getResultList().size() == 1;
    }

    /**
     * ログイン中のユーザ情報(タイムライン向け).
     */
    public String loginedUsersInfo() {
        val currentLogined = logined.clone(); // 並列セットなのでカウントする前にコピー
        return String.join(" ", new String[]{"curuser", Integer.toString(currentLogined.size()),
                                             String.join(" ", currentLogined.stream().map(User::getName).collect(Collectors.toList()))});
    }

    private final ConcurrentSkipListSet<User> logined = new ConcurrentSkipListSet<>();
    private final EntityManager em = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence").createEntityManager();
}
