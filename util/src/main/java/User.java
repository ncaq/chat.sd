package net.ncaq.chat.sd.util;

import java.net.*;
import java.security.*;

/**
 * ユーザー情報保存クラス
 * 一応パスワードをハッシュ化するが,
 * 今後セキュリティ強化を可能な設計にすることが目的なので,
 * 現在はSHA-256を1回行うだけの全く無意味なものです.
 * 実用性を考えるならセキュリティ関連は自分で書きません.
 */
public class User {
    public User(final String username, final String rawPassword) {
        String passwordStash = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(rawPassword.getBytes());
            passwordStash = md.toString();
        }
        catch(final NoSuchAlgorithmException err) {
            System.err.println(err);
            System.exit(-1);
        }
        this.username = username;
        this.password = passwordStash;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    private final String username;
    private final String password;
}
