package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * パスワードが不一致
 */
@Data
@Entity
public class PasswordInvalidMessage extends Message {
    @Override
    public static String messageType() {
        return "password invalid";
    }

    @Override
    public static Integer code() {
        return 100;
    }

    @Override
    public static String description() {
        return "";
    }
}
