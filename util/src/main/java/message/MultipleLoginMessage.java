package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * 多重ログイン
 */
@Data
@Entity
public class MultipleLoginMessage extends Message {
    @Override
    public static String messageType() {
        return "multiple login";
    }

    @Override
    public static Integer code() {
        return 101;
    }

    @Override
    public static String description() {
        return "";
    }
}
