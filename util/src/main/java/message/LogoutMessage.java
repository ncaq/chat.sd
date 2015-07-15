package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;

/**
 * ログアウト.
 */
@Entity
public class LogoutMessage extends Message {
    @Override
    public String notifyBody() {
        return String.join(" ", {"user", getPoster().getName(), getPoster().getRecentLogin().toString(), getPoster().getPostingCount().toString()});
    }

    @Override
    public String messageType() {
        return "logout";
    }

    @Override
    public static Integer code() {
        return 600;
    }

    @Override
    public static String description() {
        return "succeed(extension)";
    }
}
