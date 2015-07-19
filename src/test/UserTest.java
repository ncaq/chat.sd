package net.ncaq.chat.sd;

import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class UserTest {
    @Test
    public void testLoginQuery() {
        final User u = new User("user username pass password");
        final User t = new User("username", "password");
        assertThat(u.getName(), is(t.getName()));
        assertThat(u.getPassword(), is(t.getPassword()));
    }

    @Test
    public void testGetUserName() {
        assertThat(new User("userNameSample", "p").getName(), is("userNameSample"));
        assertThat(new User("日本語ユーザ", "p").getName(), is("日本語ユーザ"));
    }

    @Test
    public void testGetPassword() {
        final User u = new User("ユーザー名", "生パスワード");
        assertThat(u.getPassword(), is(not("生パスワード")));
        final User n = new User("ユーザー名", "生パスワード");
        assertThat(n.getPassword(), is(u.getPassword()));
    }
}
