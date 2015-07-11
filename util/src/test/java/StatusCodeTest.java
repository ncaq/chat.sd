package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.junit.Assert.*;

public class StatusCodeTest {
    @Test
    public void testStatusCodeInteger() {
        assertEquals("0 login succeed", new StatusCode(0).toString());
        assertEquals("100 password invalid", new StatusCode(100).toString());
        assertEquals("101 multiple login", new StatusCode(101).toString());
    }

    @Test
    public void testStatusCodeString() {
        assertEquals(new Integer(0), new StatusCode("0 login succeed").getCode());
        assertEquals(new Integer(100), new StatusCode("100 login succeed").getCode());
        assertEquals(new Integer(101), new StatusCode("101 login succeed").getCode());
    }
}
