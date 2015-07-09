package net.ncaq.chat.sd.util;

import java.net.*;
import java.security.*;

public class User {
    public User(final String username, final String rawPassword) {
        String passwordStash = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256"); // やらないよりはマシレベル
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
