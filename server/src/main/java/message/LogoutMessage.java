package net.ncaq.chat.sd.server.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * ログアウトした時のメッセージ.
 */
@Entity
public class LogoutMessage extends Message {
    @Override
    public String toTimeLine() {
        return String.join(" ", new String[]{"logout", "user", getPoster().getName(), messageDateFormat.format(getPoster().recentLogin().get()), getPoster().postingCountOfSession().toString()}).trim();
    }
}
