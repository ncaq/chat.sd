package net.ncaq.chat.sd.util;

import net.ncaq.chat.sd.util.message.*;
import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MessageTest {
    @Test
    public void fromCode() throws Exception {
        assertThat(Message.fromCode(0), is(instanceOf(LoginMessage.class)));
    }
}
