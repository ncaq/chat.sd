package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * CUIのクライアント.
 * 入力と出力が分けられていないため,実用性はありません.
 * サーバのテストとデバッグのために使う予定だったのですが,結局ほとんど使っていません.
 */
public class ConsoleClient {
    /**
     * 主にテスト用.
     */
    public ConsoleClient(final String hostname, final String username, final String rawPassword) throws IOException {
        server = new Connector(hostname, username, rawPassword, (newMessage) -> System.out.println(newMessage));
        Executors.newSingleThreadExecutor().execute(this::postStdInToServer);
    }

    /**
     * gradleのコマンドライン引数の取り扱いが面倒なので対話的に問い合わせます.
     */
    public ConsoleClient() throws IOException {
        final Scanner sc = new Scanner(System.in);
        System.out.println("hostname:");
        final String hostname = sc.next();
        System.out.println("username:");
        final String username = sc.next();
        System.out.println("password:");
        final String rawPassword = sc.next();

        server = new Connector(hostname, username, rawPassword, (newMessage) -> System.out.println(newMessage));
        Executors.newSingleThreadExecutor().execute(this::postStdInToServer);
    }

    /**
     * 標準入力をサーバに入力する.
     */
    private void postStdInToServer() {
        final Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            try {
                server.writeln(sc.next());
            }
            catch(IOException|InterruptedException err) {
                System.err.println(err);
                break;
            }
        }
    }

    private Connector server;
}
