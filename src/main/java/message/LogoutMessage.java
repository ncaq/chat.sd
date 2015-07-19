package net.ncaq.chat.sd.message;

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
    public Integer code() {
        return 600;
    }

    @Override
    public String description() {
        return "succeed(extension)";
    }

    @Override
    public String toTimeLineBody() {
        return String.join(" ", new String[]{"user", getPoster().getName(), messageDateFormat.format(getPoster().recentLogin().get()), getPoster().postingCount().toString()});
    }
}
