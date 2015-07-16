package net.ncaq.chat.sd;

import net.ncaq.chat.sd.message.*;
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

    @Test
    public void PasswordInvalidMessage() throws Exception {
        final Message im = Message.fromCode(100);
        assertThat(im.type(), is("password invalid"));
        assertThat(im.code(), is(100));
        assertThat(im.description(), is(""));
        assertThat(im.forTimeLine(), is("password invalid"));
        assertThat(im.status(), is("100 password invalid"));

        assertThat(Message.fromStatus("100 password invalid"), is(instanceOf(PasswordInvalidMessage.class)));
    }
}
