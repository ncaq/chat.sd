package net.ncaq.chat.sd.server.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * ログインした時のメッセージ.
 */
@Entity
@ToString(callSuper = true)
public class LoginMessage extends Message {
    @Override
    public String toTimeLine() {
        return String.join(" ", new String[]{"login", "user", getPoster().getName(), getPoster().recentLogin().map(d -> messageDateFormat.format(d)).orElse("")}).trim();
    }
}
