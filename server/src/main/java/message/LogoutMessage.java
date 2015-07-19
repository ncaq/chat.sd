package net.ncaq.chat.sd.server.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * ログアウト.
 */
@Entity
public class LogoutMessage extends Message {
    @Override
    public String type() {
        return "logout";
    }

    @Override
    public String toTimeLineBody() {
        return String.join(" ", new String[]{"user", getPoster().getName(), messageDateFormat.format(getPoster().recentLogin().get()), getPoster().postingCount().toString()});
    }
}
