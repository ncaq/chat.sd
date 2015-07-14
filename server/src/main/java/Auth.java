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
        this.userPassword.put(anonymous.getUsername(), anonymous.getPassword());
        this.userPassword.put(root.getUsername(), root.getPassword());
    }

    public StatusCode login(User u) {
        if(!u.getPassword().equals(userPassword.get(u.getUsername()))) {
            return new StatusCode(100);
        }
        else if(userLogined.contains(u.getUsername()) || !userLogined.add(u.getUsername())){ // add
            return new StatusCode(101);
        }
        else{
            return new StatusCode(0);
        }
    };

    public void addUser(User u) {
        userPassword.put(u.getUsername(), u.getPassword());
    }

    private final Map<String, String> userPassword = new ConcurrentSkipListMap<>();
    private final Set<String> userLogined = new ConcurrentSkipListSet<>();
}
