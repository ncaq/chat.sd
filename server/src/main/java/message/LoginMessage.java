package net.ncaq.chat.sd.server.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * ログイン.
 */
@Entity
public class LoginMessage extends Message {
    @Override
    public String type() {
        return "login";
    }

    @Override
    public String toTimeLineBody() {
        return String.join(" ", new String[]{"user", getPoster().getName(), getPoster().recentLogin().map(d -> messageDateFormat.format(d)).orElse("")});
    }
}
