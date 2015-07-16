package net.ncaq.chat.sd.util.message;

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
    public Integer code() {
        return 0;
    }

    @Override
    public String description() {
        return "succeed";
    }

    @Override
    public String forTimeLineBody() {
        return String.join(" ", new String[]{"user", getPoster().getName(), getPoster().getRecentLogin().toString()});
    }
}
