package net.ncaq.chat.sd.client;

import java.net.*;
import java.security.*;

class User {
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
    public final String username;
    public final String password;
}
