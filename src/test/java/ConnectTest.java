package net.ncaq.chat.sd;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import lombok.*;
import net.ncaq.chat.sd.client.*;
import net.ncaq.chat.sd.server.*;
import net.ncaq.chat.sd.*;
import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ConnectTest {
    @Test
    public void anonymousLoginIsSuccess() throws Exception {
        new CentralServer(50000);
        val q = new LinkedBlockingQueue<String>();
        val c = new Connector("localhost", "anonymous", "", (m) -> q.add(m));
        assertThat(q.take(), is(notNullValue()));
    }
}
