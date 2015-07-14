package net.ncaq.chat.sd.util;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.util.regex.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * ユーザー情報保存クラス.
 * 一応パスワードをハッシュ化するが,おそらく脆弱.
 * 学習目的で書きました.
 * 実用性を考えるならセキュリティ関連は自分で書きません.
 */
public class User {
    public User(final String username, final String rawPassword) {
        this.username = username;
        this.password = this.cryptoPassword(rawPassword);
    }

    public User(final String loginQuery) {
        final Matcher m = Pattern.compile("user\\s*(.+)\\s*pass (.+)").matcher(loginQuery);
        this.username = m.group(1);
        this.password = this.cryptoPassword(m.group(2));
    }

    private SecretKey cryptoPassword(final String rawPassword) {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(rawPassword.toCharArray(), username.getBytes("UTF-8"), 44873, 512));
        }
        catch(UnsupportedEncodingException|NoSuchAlgorithmException|IllegalArgumentException|InvalidKeySpecException err) {
            System.err.println(err);
            System.exit(-1);
            return null;
        }
    }

    /**
     * ユーザー名.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * パスワード.
     */
    public String getPassword() {
        return Base64.getEncoder().encodeToString(this.password.getEncoded());
    }

    /**
     * ログイン用文字列.
     */
    public String toString() {
        return "user " + this.getUsername() + " pass " + this.getPassword();
    }

    private final String username;
    private final SecretKey password;
}
