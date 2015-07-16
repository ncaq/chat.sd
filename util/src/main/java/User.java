package net.ncaq.chat.sd.util;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.sql.*;
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
public class User {
    @Id
    private Long id;

    private String name;

    private SecretKey password;

    /** 直近のログイン */
    @Temporal(TemporalType.DATE)
    private Date recentLogin;

    /** 発言回数 */
    private Long postingCount;

    public User(final String name, final String rawPassword) {
        this.name = name;
        this.password = this.cryptoPassword(rawPassword);
    }

    /**
     * ログイン文字列から構成.
     */
    public User(final String loginQuery) throws IllegalStateException {
        final Matcher m = Pattern.compile("user\\s*([^ ]+)\\s*pass\\s*([^ ]*)").matcher(loginQuery);
        m.matches();
        this.name = m.group(1);
        this.password = this.cryptoPassword(m.group(2));
    }

    private SecretKey cryptoPassword(final String rawPassword) {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(rawPassword.toCharArray(), name.getBytes("UTF-8"), 44873, 512));
        }
        catch(UnsupportedEncodingException|NoSuchAlgorithmException|IllegalArgumentException|InvalidKeySpecException err) {
            System.err.println(err);
            System.exit(-1);
            return null;
        }
    }
}
