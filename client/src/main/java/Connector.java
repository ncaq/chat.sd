package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import net.ncaq.chat.sd.util.*;

/**
 * クライアント側の通信を担います.
 * CUIとGUIがある以上,なるべくこちらに集約させます.
 */
public class Connector {
    public Connector(final InetAddress address, final User user) throws IOException {
        this.server = this.makeSocket(address, new Integer[]{12345, 50000});
        this.reader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.server.getOutputStream())), true);
    }

    /**
     * サーバから送られるデータを一行取得
     */
    public String readLine() throws IOException {
        return this.reader.readLine();
    }

    /**
     * サーバに一行送信
     */
    public void writeln(final String message) throws IOException {
        this.writer.println(message);
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

    private final Socket server;
    private final BufferedReader reader;
    private final PrintWriter writer;
}
