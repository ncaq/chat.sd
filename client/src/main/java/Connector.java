package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import net.ncaq.chat.sd.util.*;
import net.ncaq.chat.sd.util.message.*;

/**
 * クライアント側の通信を担います.
 * フロントエンドにCUIとGUIがある以上,なるべくこちらに集約させる.
 */
public class Connector {
    /**
     * ソケット生成.
     * 失敗すると例外吐くので再試行するかプログラム落とすこと.
     */
    public Connector(final String address, final String username, final String rawPassword, final Consumer<String> readCallback) throws ConnectException, IOException {
        this.server = this.makeSocket(InetAddress.getByName(address), new Integer[]{12345, 50000});
        this.reader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.server.getOutputStream())), true);
        this.readCallback = readCallback;

        final Message s = this.login(username, rawPassword);
        switch(s.code()) {
        case 0:
            break;
        default:
            throw new RuntimeException(s.status());
        }

        daemons.execute(() -> {
                try {
                    for(String l = reader.readLine(); l != null; l = reader.readLine()) {
                        readCallback.accept(l);
                    }
                }
                catch(final IOException err) {
                    System.err.println(err);
                }
            });
    }

    /**
     * サーバに一行送信.
     * 非同期.
     */
    public void writeln(final String message) throws IOException {
        daemons.execute(() -> {
                this.writer.println(message);
            });
    }

    private Socket makeSocket(final InetAddress serverAddress, final Integer[] ports) throws ConnectException {
        final ArrayList<Exception> errStash = new ArrayList<>();
        for(final Integer p : ports) {
            try {
                return new Socket(serverAddress, p);
            }
            catch(final Exception err) {
                errStash.add(err);
            }
        }
        throw new ConnectException(errStash.stream().map(e -> e.toString()).reduce("", (a, t) -> a + t));
    }

    private Message login(final String username, final String rawPassword) throws IOException {
        this.writeln("user " + username + " pass " + rawPassword);
        return Message.fromStatus(this.reader.readLine());
    }

    private final Socket server;
    private final Consumer<String> readCallback;

    private final BufferedReader reader;
    private final PrintWriter writer;

    private final ExecutorService daemons = Executors.newCachedThreadPool((r) -> {
            final Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
}
