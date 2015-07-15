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
    public String type() {
        return "multiple login";
    }

    @Override
    public Integer code() {
        return 101;
    }

    @Override
    public String description() {
        return "";
    }
}
