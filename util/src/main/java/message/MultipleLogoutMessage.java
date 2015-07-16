package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * 多重ログアウト
 */
@Data
@Entity
public class MultipleLogoutMessage extends Message {
    @Override
    public String type() {
        return "multiple logout";
    }

    @Override
    public Integer code() {
        return 601;
    }

    @Override
    public String description() {
        return "(extension)";
    }

    @Override
    public String forTimeLineBody() {
        return "";
    }
}
