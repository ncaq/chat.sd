package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testGetUserName() {
        assertEquals(new User("userNameSample", "p").getUsername(), "userNameSample");
        assertEquals(new User("日本語ユーザ", "p").getUsername(), "日本語ユーザ");
    }

    @Test
    public void testGetPassword() {
        assertNotEquals(new User("ユーザー名", "生パスワード").getUsername(), "生パスワード");
    }

    @Test
    public void testToString() {
        final User u = new User("ユ", "パ");
        final String hashedPassword = u.getPassword();
        assertEquals(u.toString(), "user ユ pass " + hashedPassword);
    }
}
