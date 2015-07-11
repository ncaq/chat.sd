package net.ncaq.chat.sd;

import java.io.*;
import java.net.*;
import net.ncaq.chat.sd.client.*;
import net.ncaq.chat.sd.server.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ConnectTest {
    @Test
    public void testStartSession() throws IOException, UnknownHostException {
        InputStream stdin = System.in;
        try {
            new ChatServer(50000);

            final PipedOutputStream pipe = new PipedOutputStream();
            System.setIn(new PipedInputStream(pipe));
            final PrintWriter pin = new PrintWriter(new BufferedOutputStream(pipe));

            new ConsoleClient("localhost", "anonymous", "");
        } finally {
            System.setIn(stdin);
        }
    }
}
