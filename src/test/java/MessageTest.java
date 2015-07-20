package net.ncaq.chat.sd;

import net.ncaq.chat.sd.message.*;
import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MessageTest {
    @Test
    public void chatMessage() throws Exception {
        final Message cm = new ChatMessage();
        cm.setPoster(new User("hoge", "huga"));
        cm.setBody("foo");
        assertThat(cm.type(), is("chat"));
        assertThat(cm.toTimeLine(), is("chat hoge foo"));
    }

    @Test
    public void loginMessage() throws Exception {
        final Message lm = new LoginMessage();
        lm.setPoster(new User("hoge", "huga"));
        assertThat(lm.type(), is("login"));
        assertThat(lm.toTimeLine(), startsWith("login user hoge"));
    }
}
