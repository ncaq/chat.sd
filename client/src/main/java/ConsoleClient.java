package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import net.ncaq.chat.sd.util.*;

/**
 * GUI less client
 * for test and debug
 */
public class ConsoleClient {
    public ConsoleClient() throws UnknownHostException, IOException {
        final Scanner sc = new Scanner(System.in);
        System.out.println("hostname:");
        final String hostname = sc.next();
        System.out.println("username:");
        final String username = sc.next();
        System.out.println("password:");
        final String rawPassword = sc.next();

        // D言語なら好きにthisを呼び出せるんですが
        final Connector server = new Connector(InetAddress.getByName(hostname), new User(username, rawPassword));
        new Thread(() -> {
                while(sc.hasNext()) {
                    try {
                        server.writeln(sc.next());
                    }
                    catch(final IOException err) {
                        System.err.println(err);
                        break;
                    }}}).start();
    }

    public ConsoleClient(final String hostname, final String username ,final String rawPassword) throws UnknownHostException, IOException {
        final Scanner sc = new Scanner(System.in);
        final Connector server = new Connector(InetAddress.getByName(hostname), new User(username, rawPassword));
        new Thread(() -> {
                while(sc.hasNext()) {
                    try {
                        server.writeln(sc.next());
                    }
                    catch(final IOException err) {
                        System.err.println(err);
                        break;
                    }}}).start();
    }
}
