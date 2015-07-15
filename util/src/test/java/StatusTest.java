package net.ncaq.chat.sd.util;

import org.junit.*;
import static net.ncaq.chat.sd.util.Status.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class StatusTest {
    @Test
    public void ToString() {
        assertThat(LOGIN_SUCCEED   .toString(), is(  "0 login succeed"));
        assertThat(PASSWORD_INVALID.toString(), is("100 password invalid"));
        assertThat(MULTIPLE_LOGIN  .toString(), is("101 multiple login"));
        assertThat(LOGOUT_SUCCEED  .toString(), is("600 logout succeed(extension)"));
        assertThat(MULTIPLE_LOGOUT .toString(), is("601 multiple logout(extension)"));
    }

    @Test
    public void fromFullMessage() {
        assertThat("  0 login succeed"   , is(LOGIN_SUCCEED));
        assertThat("100 password invalid", is(PASSWORD_INVALID));
        assertThat("101 multiple login"  , is(MULTIPLE_LOGIN));
        assertThat("600 logout succeed"  , is(LOGOUT_SUCCEED));
        assertThat("601 multiple logout" , is(MULTIPLE_LOGOUT));
    }

    @Test
    public void Equals() {
        assertThat(LOGIN_SUCCEED, is(LOGIN_SUCCEED));
        assertThat(LOGIN_SUCCEED, is(not(PASSWORD_INVALID)));
    }
}
