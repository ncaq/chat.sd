package net.ncaq.chat.sd;

import java.io.*;
import java.net.*;
import net.ncaq.chat.sd.client.*;
import net.ncaq.chat.sd.server.*;
import net.ncaq.chat.sd.util.*;
import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ConnectTest {
    @Test
    public void anonymousLoginIsSuccess() throws Exception {
        new ChatServer(50000);
        final User u = new User("anonymous", "");
        final Connector c = new Connector("localhost", u);
        assertThat(c.readLine(), is(new StatusCode(0).toString()));
    }
}
