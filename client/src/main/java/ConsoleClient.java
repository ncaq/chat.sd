package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * CUIのクライアント.
 * 入力と出力が分けられていないため,実用性はありません.
 * サーバのテストとデバッグのために使います.
 */
public class ConsoleClient {
    /**
     * 主にテスト用.
     */
    public ConsoleClient(final String hostname, final String username, final String rawPassword) {
        try {
            final Connector server = new Connector(hostname, username, rawPassword);
            Executors.newSingleThreadExecutor().execute(getStreamToStdOut(server));
            Executors.newSingleThreadExecutor().execute(postStdInToServer(server));
        }
        catch(final Exception err) {
            System.err.println(err);
            System.exit(-1);
        }
    }

    /**
     * gradleのコマンドライン引数の取り扱いが面倒なので対話的に問い合わせます.
     */
    public ConsoleClient() {
        try {
            final Scanner sc = new Scanner(System.in);
            System.out.println("hostname:");
            final String hostname = sc.next();
            System.out.println("username:");
            final String username = sc.next();
            System.out.println("password:");
            final String rawPassword = sc.next();

            final Connector server = new Connector(hostname, username, rawPassword);
            Executors.newSingleThreadExecutor().execute(getStreamToStdOut(server));
            Executors.newSingleThreadExecutor().execute(postStdInToServer(server));
        }
        catch(final Exception err) {
            System.err.println(err);
            System.exit(-1);
        }
    }

    /**
     * サーバの出力を標準出力する関数.
     */
    private Runnable getStreamToStdOut(final Connector server) {
        return (() -> {
                try {
                    for(String l = server.readLine(); l != null; l = server.readLine()) {
                        System.out.println(l);
                    }}
                catch(final IOException err) {
                    System.err.println(err);
                }});
    }

    /**
     * 標準入力をサーバに入力する関数.
     */
    private Runnable postStdInToServer(final Connector server) {
        final Scanner sc = new Scanner(System.in);
        return (() -> {
                while(sc.hasNext()) {
                    try {
                        server.writeln(sc.next());
                    }
                    catch(final IOException err) {
                        System.err.println(err);
                        break;
                    }}});
    }
}
