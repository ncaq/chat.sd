package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testGetUserName() {
        assertEquals("userNameSample", new User("userNameSample", "p").getUsername());
        assertEquals("日本語ユーザ", new User("日本語ユーザ", "p").getUsername());
    }

    @Test
    public void testGetPassword() {
        assertNotEquals("生パスワード", new User("ユーザー名", "生パスワード").getUsername());
    }

    @Test
    public void testToString() {
        final User u = new User("ユ", "パ");
        final String hashedPassword = u.getPassword();
        assertEquals("user ユ pass " + hashedPassword, u.toString());
    }
}
