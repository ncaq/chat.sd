package net.ncaq.chat.sd.util;

import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MessageTest {
    @Test
    public void fromCode() {
        assertThat(Message.fromCode(0), is(new LoginMessage()));
    }
}
