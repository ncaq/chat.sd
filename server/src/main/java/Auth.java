package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import net.ncaq.chat.sd.util.*;

/**
 * ログインとかログアウトとか管理します
 * スレッドセーフ
 */
public class Auth {
    public Auth() {
        final User anonymous = new User("anonymous", "");
        final User root = new User("root", "特に特権とかない");
        this.userPassword.put(anonymous.getUsername(), anonymous.getPassword());
        this.userPassword.put(root.getUsername(), root.getPassword());
    }

    public Status login(final User u) {
        if(!this.contains(u)) {
            return PASSWORD_INVALID;
        }
        else if(userLogined.contains(u.getUsername()) || !userLogined.add(u.getUsername())) { // add
            return MULTIPLE_LOGIN;
        }
        else {
            return LOGIN_SUCCEED;
        }
    };

    public Status logout(final User u) {
        if(!this.contains(u)) {
            return PASSWORD_INVALID;
        }
        else if(!this.userLogined.remove(u.getUsername())) { // remove
            return MULTIPLE_LOGOUT;
        }
        else {
            return LOGOUT_SUCCEED;
        }
    }

    public void addUser(final User u) {
        userPassword.put(u.getUsername(), u.getPassword());
    }

    public boolean contains(final User u) {
        return u.getPassword().equals(userPassword.get(u.getUsername()));
    }

    private final Map<String, String> userPassword = new ConcurrentSkipListMap<>();
    private final Set<String> userLogined = new ConcurrentSkipListSet<>();
}
