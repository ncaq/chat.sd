package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;

/**
 * ログイン.
 */
@Entity
public class LoginMessage extends Message {
    @Override
    public String notifyBody() {
        return String.join(" ", {"user", getPoster().getName(), getPoster().getRecentLogin().toString()});
    }

    @Override
    public String messageType() {
        return "login";
    }

    @Override
    public static Integer code() {
        return 1;
    }

    @Override
    public static String description() {
        return "succeed";
    }
}
