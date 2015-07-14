package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class PostTimeLineR implements Runnable {
    public PostTimeLineR(final BufferedReader client, final ChatServer server, final User user) throws IOException {
        this.client = client;
        this.server = server;
        this.user = user;
    }

    public void run() {
        for(;;) {
            try {
                final String l = this.client.readLine();
                if(l == null) {
                    break;
                }
                else {
                    this.server.broadcast("chat " + user.getUsername() + " " + l);
                }
            }
            catch(final IOException err) {
                System.err.println(err);
                break;
            }
        }
    }

    private final BufferedReader client;
    private final ChatServer server;
    private final User user;
}
