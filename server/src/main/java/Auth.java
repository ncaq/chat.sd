package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import net.ncaq.chat.sd.util.*;

public class Auth {
    public Auth() {
        final User anonymous = new User("anonymous", "");
        final User root = new User("root", "特に特権とかない");
        this.userPassword.put(anonymous.username, anonymous.password);
        this.userPassword.put(root.username, root.password);
    }

    public StatusCode login(User u) {
        if(!userPassword.get(u.username).equals(u.password)) {
            return new StatusCode(100);
        }
        else if(userLogined.contains(u.username) || !userLogined.add(u.username)){
            return new StatusCode(101);
        }
        else{
            return new StatusCode(0);
        }
    };

    private final Map<String, String> userPassword = new ConcurrentSkipListMap<>();
    private final Set<String> userLogined = new ConcurrentSkipListSet<>();
}
