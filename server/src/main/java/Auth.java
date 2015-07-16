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
        Message m = !this.correctUser(u) ?
            new PasswordInvalidMessage() :
            logined.contains(u) || !logined.add(u) ? // add
            new MultipleLoginMessage() :
            new LoginMessage();
        m.setPoster(u);
        return m;
    };

    public Message logout(final User u) {
        Message m = !this.correctUser(u) ?
            new PasswordInvalidMessage() :
            !this.logined.remove(u.getName()) ? // remove
            new MultipleLogoutMessage() :
            new LogoutMessage();
        m.setPoster(u);
        return m;
    }

    /**
     * ユーザーを追加
     * @todo
     */
    public void addUser(final User u) {
        // userPassword.put(u.getName(), u.getPassword().toString());
    }

    /**
     * ユーザがデータベースに存在するか.
     * @return true 存在する
     * @todo
     */
    public boolean correctUser(final User u) {
        // return u.getPassword().toString().equals(userPassword.get(u.getName()));
        return true;
    }

    private final Set<User> logined = new ConcurrentSkipListSet<>();
}
