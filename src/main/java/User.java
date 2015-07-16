package net.ncaq.chat.sd;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.sql.*;
import java.util.Base64;
import java.util.regex.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.persistence.*;
import lombok.*;

/**
 * ユーザー情報保存クラス.
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
        final Matcher m = Pattern.compile("user\\s*([^ ]+)\\s*pass\\s*([^ ]*)").matcher(loginQuery);
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
     * todo
     */
    public Date recentLogin() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * 発言回数.
     * todo
     */
    public Long postingCount() {
        return 0l;
    }
}
