package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.junit.Assert.*;

public class StatusCodeTest {
    @Test
    public void StatusCode_Integer() {
        assertEquals(new StatusCode(0).toString(), "0 login succeed");
        assertEquals(new StatusCode(100).toString(), "100 password invalid");
        assertEquals(new StatusCode(101).toString(), "101 multiple login");
    }

    @Test
    public void StatusCode_String() {
        assertEquals(new StatusCode("0 login succeed").getCode(), new Integer(0));
        assertEquals(new StatusCode("100 login succeed").getCode(), new Integer(100));
        assertEquals(new StatusCode("101 login succeed").getCode(), new Integer(101));
    }
}
