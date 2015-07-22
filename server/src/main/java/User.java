package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.util.regex.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.persistence.*;
import javax.persistence.criteria.*;
import lombok.*;
import net.ncaq.chat.sd.server.message.*;

/**
 * ユーザー情報.
 * メッセージデータベースからユーザに関する情報も取り出せる.
 * JPAによりデータベースに保存する.
 * 一応パスワードをハッシュ化するが,おそらく脆弱.
 */
@Data
@Entity
public class User implements Comparable<User> {
    @Id
    private String name;

    private String password;

    /**
     * 平文パスワードをハッシュ化して格納.
     */
    public void setPassword(final String rawPassword) {
        this.password = this.cryptoPassword(this.name, rawPassword);
    }

    /**
     * for JPA.
     */
    public User() {
    }

    public User(final String name, final String rawPassword) {
        this.setName(name);
        this.setPassword(rawPassword);
    }

    /**
     * ログイン文字列から構成.
     */
    public User(final String loginQuery) throws IllegalStateException {
        final Matcher m = Pattern.compile("user\\s*(\\S*)\\s*pass\\s*(\\S*)").matcher(loginQuery);
        m.matches();
        setName(m.group(1));
        setPassword(m.group(2));
    }

    private static String cryptoPassword(final String name, final String rawPassword) {
        try {
            return Base64.getEncoder().encodeToString(
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(
                    new PBEKeySpec(rawPassword.toCharArray(), name.getBytes("UTF-8"), 44873, 512)).getEncoded());
        }
        catch(UnsupportedEncodingException|NoSuchAlgorithmException|IllegalArgumentException|InvalidKeySpecException err) {
            System.err.println(err);
            System.exit(-1);
            return null;
        }
    }

    /**
     * SkipListに格納するため比較を実装する.
     */
    @Override
    public int compareTo(final User take) {
        val nc = this.name.compareTo(take.name);
        return (nc == 0) ? this.password.toString().compareTo(this.password.toString()) :
            nc;
    }

    /**
     * 直近のログイン.
     * @return ログインしたことがない @code{Optional.empty()}.
     */
    public Optional<Date> recentLogin() {
        try {
            val em = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence").createEntityManager();
            val cb = em.getCriteriaBuilder();
            val q = cb.createQuery(LoginMessage.class);
            val root = q.from(LoginMessage.class);
            q.select(root)
                .where(cb.equal(root.get(LoginMessage_.poster), this))
                .orderBy(cb.desc(root.get(LoginMessage_.submit)));
            return Optional.of(em.createQuery(q).getResultList().get(0).getSubmit());
        }
        catch(final ArrayIndexOutOfBoundsException exc) {
            return Optional.empty();
        }
    }

    /**
     * 発言回数.
     */
    public Long postingCount() {
        val em = Persistence.createEntityManagerFactory("net.ncaq.chat.sd.persistence").createEntityManager();
        val cb = em.getCriteriaBuilder();
        val q = cb.createQuery(Long.class);
        val root = q.from(ChatMessage.class);
        q.select(cb.count(root)).where(cb.equal(root.get(ChatMessage_.poster), this));
        return em.createQuery(q).getSingleResult();
    }
}
