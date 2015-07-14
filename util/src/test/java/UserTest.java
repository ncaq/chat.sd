package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UserTest {
    @Test
    public void testLoginQuery() {
        final User u = new User("user username pass password");
        final User t = new User("username", "password");
        assertThat(u.getUsername(), is(t.getUsername()));
        assertThat(u.getPassword(), is(t.getPassword()));
        assertThat(u, is(t));
    }

    @Test
    public void testGetUserName() {
        assertThat(new User("userNameSample", "p").getUsername(), is("userNameSample"));
        assertThat(new User("日本語ユーザ", "p").getUsername(), is("日本語ユーザ"));
    }

    @Test
    public void testGetPassword() {
        final User u = new User("ユーザー名", "生パスワード");
        assertThat(u.getPassword(), is(not("生パスワード")));
        final User n = new User("ユーザー名", "生パスワード");
        assertThat(n.getPassword(), is(u.getPassword()));
    }

    @Test
    public void testToString() {
        final User u = new User("ユ", "パ");
        final String hashedPassword = u.getPassword();
        assertThat(u.toString(), is("user ユ pass " + hashedPassword));
    }
}
