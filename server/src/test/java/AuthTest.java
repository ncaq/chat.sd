package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class AuthTest {
    @Test
    public void existUserIs1() throws Exception {
        final Auth a = new Auth();
        a.addUser(new User("sampleUser", "pass"));
        assertThat(a.login(new User("sampleUser", "pass")), is(new StatusCode(1)));
    }

    @Test
    public void nothingUserIs100() throws Exception {
        final Auth a = new Auth();
        assertThat(a.login(new User("sampleUser", "pass")), is(new StatusCode(100)));
    }

    @Test
    public void multipleLoginIs101() throws Exception {
        final Auth a = new Auth();
        a.addUser(new User("sampleUser", "pass"));
        assertThat(a.login(new User("sampleUser", "pass")), is(new StatusCode(1)));
        assertThat(a.login(new User("sampleUser", "pass")), is(new StatusCode(101)));
    }
}
