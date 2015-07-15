package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import net.ncaq.chat.sd.util.*;
import net.ncaq.chat.sd.util.message.*;

/**
 * ログインとかログアウトとか管理します
 * スレッドセーフ
 */
public class Auth {
    public Auth() {
        final User anonymous = new User("anonymous", "");
        final User root = new User("root", "特に特権とかない");
        // this.userPassword.put(anonymous.getName(), anonymous.getPassword().toString());
        // this.userPassword.put(root.getName(), root.getPassword().toString());
    }

    public Message login(final User u) {
        if(!this.contains(u)) {
            return PASSWORD_INVALID;
        }
        else if(logined.contains(u.getName()) || !logined.add(u.getName())) { // add
            return MULTIPLE_LOGIN;
        }
        else {
            return LOGIN_SUCCEED;
        }
    };

    public Message logout(final User u) {
        if(!this.contains(u)) {
            return PASSWORD_INVALID;
        }
        else if(!this.logined.remove(u.getName())) { // remove
            return MULTIPLE_LOGOUT;
        }
        else {
            return LOGOUT_SUCCEED;
        }
    }

    public void addUser(final User u) {
        // userPassword.put(u.getName(), u.getPassword().toString());
    }

    public boolean contains(final User u) {
        // return u.getPassword().toString().equals(userPassword.get(u.getName()));
    }

    private final Set<User> logined = new ConcurrentSkipListSet<>();
}
