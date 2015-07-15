package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class StatusTest {
    @Test
    public void testStatusInteger() {
        assertThat(new Status(0).toString(), is("0 login succeed"));
        assertThat(new Status(100).toString(), is("100 password invalid"));
        assertThat(new Status(101).toString(), is("101 multiple login"));
    }

    @Test
    public void testStatusString() {
        assertThat(new Status("0 login succeed").getCode(), is(new Integer(0)));
        assertThat(new Status("100 login succeed").getCode(), is(new Integer(100)));
        assertThat(new Status("101 login succeed").getCode(), is(new Integer(101)));
    }

    @Test
    public void testEquals() {
        assertThat(new Status(0), is(new Status(0)));
        assertThat(new Status(0), equalTo(new Status(0)));
    }
}
