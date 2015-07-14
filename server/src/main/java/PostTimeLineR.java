package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class PostTimeLineR implements Runnable {
    public PostTimeLineR(final BufferedReader client, final ChatServer server) throws IOException {
        this.client = client;
        this.server = server;
    }

    public void run() {
        for(;;) {
            try {
                final String l = this.client.readLine();
                if(l == null) {
                    break;
                }
                else {
                    this.server.broadcast(l);
                }
            }
            catch(IOException|InterruptedException err) {
                System.err.println(err);
                break;
            }
        }
    }

    private final BufferedReader client;
    private final ChatServer server;
}
