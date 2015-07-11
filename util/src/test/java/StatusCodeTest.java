package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.junit.Assert.*;

public class StatusCodeTest {
    @Test
    public void testStatusCodeInteger() {
        assertThat(new StatusCode(0).toString() is("0 login succeed"));
        assertThat(new StatusCode(100).toString() is("100 password invalid"));
        assertThat(new StatusCode(101).toString() is("101 multiple login"));
    }

    @Test
    public void testStatusCodeString() {
        assertThat(new StatusCode("0 login succeed").getCode(), is(new Integer(0)));
        assertThat(new StatusCode("100 login succeed").getCode(), is(new Integer(100)));
        assertThat(new StatusCode("101 login succeed").getCode(), is(new Integer(101)));
    }
}
