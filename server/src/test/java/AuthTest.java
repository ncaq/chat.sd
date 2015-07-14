package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import net.ncaq.chat.sd.util.*;
import org.junit.*;
import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class AuthTest {
    @Test
    public void existUserIs0() throws Exception {
        final Auth a = new Auth();
        a.addUser(new User("sampleUser", "pass"));
        assertThat(a.login(new User("sampleUser", "pass")), is(new StatusCode(0)));
    }

    @Test
    public void anonymousLogin() throws Exception {
        assertThat(new Auth().login(new User("anonymous", "")), is(new StatusCode(0)));
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
    }

    @Test
    public void loginAndLogoutAndLogin() {
        final Auth a = new Auth();
        a.addUser(new User("sampleUser", "pass"));
        assertThat(a.login(new User("sampleUser", "pass")), is(new StatusCode(0)));
        assertThat(a.logout(new User("sampleUser", "pass")), is(new StatusCode(600)));
        assertThat(a.login(new User("sampleUser", "pass")), is(new StatusCode(0)));
    }

    @Test
    public void multiThreadLoginAndLogout() throws Exception {
        final Auth a = new Auth();
        final User u = new User("sampleUser", "pass");
        a.addUser(u);

        final int threads = 100;

        final ExecutorService lio = Executors.newFixedThreadPool(2 * threads + 1);

        Collection<Callable<StatusCode>> cl = new ConcurrentLinkedQueue<>();
        for(int i = 0; i < threads; ++i) {
            cl.add(() -> a.login(u));
            cl.add(() -> a.logout(u));
        }

        List<Future<StatusCode>> fsl = lio.invokeAll(cl);
        fsl.forEach(f -> {
                try {
                    f.get();
                }
                catch(final Exception err) {
                    fail(err.getMessage());
                }
            }); // wait
        fsl.add(lio.submit(() -> a.logout(u)));

        final List<StatusCode> sl = fsl.stream().map(f -> {
                try {
                    return f.get();
                }
                catch(final Exception err) {
                    fail(err.getMessage());
                    return null;
                }
            }).collect(Collectors.toList());

        assertThat("status list: " + Arrays.deepToString(sl.toArray()),
                   sl.stream().filter(s -> s.equals(new StatusCode(0))).count() -
                   sl.stream().filter(s -> s.equals(new StatusCode(600))).count(),
                   is(0L));
    }
}
