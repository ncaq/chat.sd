package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import lombok.*;
import net.ncaq.chat.sd.*;
import static net.ncaq.chat.sd.Status.*;

/**
 * クライアント側の通信を担います.
 * フロントエンドにCUIとGUIがある以上,なるべくこちらに集約させる.
 */
public class Connector {
    /**
     * ソケット生成.
     * 失敗すると例外吐くので再試行するかプログラム落とすこと.
     */
    public Connector(final String address, final String username, final String rawPassword, final Consumer<String> readCallback) throws IOException, ConnectException {
        this.server = this.makeSocket(InetAddress.getByName(address), new Integer[]{50000, 12345, 15000});
        this.reader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.server.getOutputStream())), true);
        this.readCallback = readCallback;

        val s = this.login(username, rawPassword);
        switch(s) {
        case LOGIN_SUCCEED:
            break;
        default:
            throw new ConnectException(s.toString());
        }

        // 正常時のみ
        daemons.execute(this::readLoop);
        daemons.execute(this::writeLoop);
    }

    /**
     * サーバに一行送信準備.
     */
    public void writeln(final String newMessage) throws IOException, InterruptedException {
        newMessageBox.put(newMessage);
    }

    private Socket makeSocket(final InetAddress serverAddress, final Integer[] ports) throws ConnectException {
        final ArrayList<Exception> errStash = new ArrayList<>();
        for(final Integer p : ports) {
            try {
                val isa = new InetSocketAddress(serverAddress, p);
                val s = new Socket();
                s.connect(isa, 5000);
                return s;
            }
            catch(final Exception err) {
                errStash.add(err);
            }
        }
        throw new ConnectException(errStash.stream().map(e -> e.toString()).reduce("", (a, t) -> a + t));
    }

    private Status login(final String username, final String rawPassword) throws IOException {
        this.writer.println("user " + username + " pass " + rawPassword);
        return Status.of(this.reader.readLine());
    }

    private void readLoop() {
        try {
            for(String l = reader.readLine(); l != null; l = reader.readLine()) {
                readCallback.accept(MessageTranslator.toHuman("ja", l));
            }
        }
        catch(final IOException err) {
            System.err.println(err);
        }
    }

    private void writeLoop() {
        try {
            for(String m = newMessageBox.take(); m != null; m = newMessageBox.take()) {
                writer.println(m);
            }
        }
        catch(final InterruptedException err) {
            System.err.println(err);
        }
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

    private final LinkedBlockingQueue<String> newMessageBox = new LinkedBlockingQueue<>();
}
