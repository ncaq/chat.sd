package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * パスワードが不一致
 */
@Entity
public class PasswordInvalidMessage extends Message {
    @Override
    public String type() {
        return "password invalid";
    }

    @Override
    public Integer code() {
        return 100;
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public String forTimeLineBody() {
        return "";
    }
}
