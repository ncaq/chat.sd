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
    }

    public Message login(final User u) {
        Message m = !this.contains(u) ?
            new PasswordInvalidMessage() :
            logined.contains(u) || !logined.add(u) ? // add
            new MultipleLoginMessage() :
            new LoginMessage();
        m.setPoster(u);
        return m;
    };

    public Message logout(final User u) {
        Message m = !this.contains(u) ?
            new PasswordInvalidMessage() :
            !this.logined.remove(u.getName()) ? // remove
            new MultipleLogoutMessage() :
            new LogoutMessage();
        m.setPoster(u);
        return m;
    }

    public void addUser(final User u) {
        // userPassword.put(u.getName(), u.getPassword().toString());
    }

    public boolean contains(final User u) {
        // return u.getPassword().toString().equals(userPassword.get(u.getName()));
        /** @todo */
        return true;
    }

    private final Set<User> logined = new ConcurrentSkipListSet<>();
}
