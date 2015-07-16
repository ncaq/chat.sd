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

    @Test
    public void ChatMessage() throws Exception {
        final Message cm = Message.fromCode(200);
        cm.setPoster(new User("hoge", "huga"));
        cm.setBody("foo");
        assertThat(cm.type(), is("chat"));
        assertThat(cm.code(), is(200));
        assertThat(cm.description(), is("ok(extension)"));
        assertThat(cm.forTimeLine(), startsWith("chat hoge foo"));
    }

    @Test
    public void LoginMessage() throws Exception {
        final Message lm = Message.fromCode(0);
        lm.setPoster(new User("hoge", "huga"));
        assertThat(lm.type(), is("login"));
        assertThat(lm.code(), is(0));
        assertThat(lm.description(), is("succeed"));
        assertThat(lm.forTimeLine(), startsWith("login user hoge"));
    }
}
