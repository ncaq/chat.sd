package net.ncaq.chat.sd.server;

import net.ncaq.chat.sd.message.*;
import org.junit.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MessageTest {
    @Test
    public void of() throws Exception {
        assertThat(Message.of(0), is(instanceOf(LoginMessage.class)));
    }

    @Test
    public void ChatMessage() throws Exception {
        final Message cm = Message.of(200);
        cm.setPoster(new User("hoge", "huga"));
        cm.setBody("foo");
        assertThat(cm.type(), is("chat"));
        assertThat(cm.code(), is(200));
        assertThat(cm.description(), is("ok(extension)"));
        assertThat(cm.toTimeLine(), is("chat hoge foo"));

        assertThat(Message.of("200 chat ok"), is(instanceOf(ChatMessage.class)));
    }

    @Test
    public void LoginMessage() throws Exception {
        final Message lm = Message.of(0);
        lm.setPoster(new User("hoge", "huga"));
        assertThat(lm.type(), is("login"));
        assertThat(lm.code(), is(0));
        assertThat(lm.description(), is("succeed"));
        assertThat(lm.toTimeLine(), startsWith("login user hoge"));
        assertThat(lm.status(), is("0 login succeed"));

        assertThat(Message.of("0 login succeed"), is(instanceOf(LoginMessage.class)));
    }

    @Test
    public void PasswordInvalidMessage() throws Exception {
        final Message im = Message.of(100);
        assertThat(im.type(), is("password invalid"));
        assertThat(im.code(), is(100));
        assertThat(im.description(), is(""));
        assertThat(im.toTimeLine(), is("password invalid"));
        assertThat(im.status(), is("100 password invalid"));

        assertThat(Message.of("100 password invalid"), is(instanceOf(PasswordInvalidMessage.class)));
    }
}
