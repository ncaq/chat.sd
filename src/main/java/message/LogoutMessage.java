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
    public String forTimeLineBody() {
        return String.join(" ", new String[]{"user", getPoster().getName(), getPoster().recentLogin().get().toString(), getPoster().postingCount().toString()});
    }
}
