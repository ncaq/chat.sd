package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testGetUserName() {
        assertThat(new User("userNameSample", "p").getUsername(), is("userNameSample"));
        assertThat(new User("日本語ユーザ", "p").getUsername(), is("日本語ユーザ"));
    }

    @Test
    public void testGetPassword() {
        assertThat(new User("ユーザー名", "生パスワード").getUsername(), isNot("生パスワード"));
    }

    @Test
    public void testToString() {
        final User u = new User("ユ", "パ");
        final String hashedPassword = u.getPassword();
        assertThat(u.toString(), is("user ユ pass " + hashedPassword));
    }
}
