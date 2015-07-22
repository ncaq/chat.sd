package net.ncaq.chat.sd.server.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * ログインした時のメッセージ.
 */
@Entity
public class LoginMessage extends Message {
    @Override
    public String type() {
        return "login";
    }

    @Override
    public String toTimeLine() {
        return String.join(" ", new String[]{this.type(), "user", getPoster().getName(), getPoster().recentLogin().map(d -> messageDateFormat.format(d)).orElse("")}).trim();
    }
}
