package net.ncaq.chat.client;

import java.io.*;
import java.net.*;

public class Connector {
    public Connector(final InetAddress serverAddress) throws IOException {
        this.server = this.makeSocket(serverAddress, new Integer[]{12345, 50000});
        this.reader = new BufferedReader(new InputStreamReader(this.server.getInputStream()));
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.server.getOutputStream())), true);
    }

    public String readLine() throws IOException {
        return this.reader.readLine();
    }

    public void writeln(final String message) throws IOException {
        this.writer.println(message);
    }

    private Socket makeSocket(final InetAddress serverAddress, final Integer[] ports) throws ConnectException {
        for(final Integer p : ports) {
            try {
                return new Socket(serverAddress, p);
            }
            catch(final Exception err) {
            }
        }
        throw new ConnectException("接続できませんでした");
    }

    private final Socket server;
    private final BufferedReader reader;
    private final PrintWriter writer;
}
