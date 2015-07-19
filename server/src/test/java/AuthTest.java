package net.ncaq.chat.sd.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import lombok.*;
import net.ncaq.chat.sd.*;
import net.ncaq.chat.sd.server.*;
import org.junit.*;
import static junit.framework.Assert.*;
import static net.ncaq.chat.sd.Status.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class AuthTest {
    @Test
    public void anonymousLoginIs0() throws Exception {
        val s = new Auth().login(new User("anonymous", ""));
        assertThat(s, is(LOGIN_SUCCEED));
        assertThat(s.toString(), is("0 login succeed"));
    }

    @Test
    public void nothingUserIs100() throws Exception {
        assertThat(new Auth().login(new User("nothingUser", "nonono")), is(PASSWORD_INVALID));
    }

    @Test
    public void multipleLoginIs101() throws Exception {
        val a = new Auth();
        assertThat(a.login(new User("anonymous", "")), is(LOGIN_SUCCEED));
        assertThat(a.login(new User("anonymous", "")), is(MULTIPLE_LOGIN));
    }

    @Test
    public void loginAndLogoutAndLogin() {
        val a = new Auth();
        val u = new User("anonymous", "");
        assertThat(a.login(u), is(LOGIN_SUCCEED));
        assertThat(a.logout(u), is(LOGOUT_SUCCEED));
        assertThat(a.login(u), is(LOGIN_SUCCEED));
    }

    @Test
    public void multiThreadLoginAndLogout() throws Exception {
        final Auth a = new Auth();
        final User u = new User("anonymous", "");
        a.addUser(u);

        final int threads = 100;

        final ExecutorService lio = Executors.newFixedThreadPool(2 * threads + 1);

        Collection<Callable<Status>> cl = new ConcurrentLinkedQueue<>();
        for(int i = 0; i < threads; ++i) {
            cl.add(() -> a.login(u));
            cl.add(() -> a.logout(u));
        }

        List<Future<Status>> fsl = lio.invokeAll(cl);
        fsl.forEach(f -> {
                try {
                    f.get();
                }
                catch(final Exception err) {
                    fail(err.getMessage());
                }
            }); // wait
        fsl.add(lio.submit(() -> a.logout(u)));

        final List<Status> sl = fsl.stream().map(f -> {
                try {
                    return f.get();
                }
                catch(final Exception err) {
                    fail(err.getMessage());
                    return null;
                }
            }).collect(Collectors.toList());

        assertThat("status list: " + Arrays.deepToString(sl.toArray()),
                   sl.stream().filter(s -> s.equals(LOGIN_SUCCEED)).count() -
                   sl.stream().filter(s -> s.equals(LOGOUT_SUCCEED)).count(),
                   is(0L));
    }
}
