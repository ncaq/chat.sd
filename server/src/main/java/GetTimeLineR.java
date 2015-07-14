package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class GetTimeLineR implements Runnable {
    public GetTimeLineR(final PrintWriter client, final LinkedBlockingQueue<String> messageBox) throws IOException {
        this.client = client;
        this.messageBox = messageBox;
    }

    public void run() {
        for(;;) {
            try {
                client.println(messageBox.take());
            }
            catch(final InterruptedException err) {
                System.err.println(err);
                break;
            }}
    }

    private final PrintWriter client;
    private final LinkedBlockingQueue<String> messageBox;
}
