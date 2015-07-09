package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import net.ncaq.chat.sd.util.*;

public class Auth {
    public Auth() {
        this.userPassword = new ConcurrentSkipListMap<>();
        final User anonymous = new User("anonymous", "");
        final User root = new User("root", "特に特権とかない");
        this.userPassword.put(anonymous.username, anonymous.password);
        this.userPassword.put(root.username, root.password);

        this.userLogined = new ConcurrentSkipListSet<>();
    }

    public String login(User u) {
        if(!userPassword.get(u.username).equals(u.password)) {
            return "100 password invalid";
        }
        else if(userLogined.contains(u.username) || !userLogined.add(u.username)){
            return "101 multiple login";
        }
        else{
            return "0 login succeed";
        }
    };

    private final Map<String, String> userPassword;
    private final Set<String> userLogined;
}
