package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * チャットによる会話メッセージ
 */
@Data
@Entity
public class ChatMessage extends Message {
    @Override
    public String notifyBody() {
        return String.join(" ", new String[]{getPoster().getName(), getBody()});
    }

    @Override
    public String messageType() {
        return "chat";
    }

    @Override
    public static Integer code() {
        return 200;
    }

    @Override
    public static String description() {
        return "ok(extension)";
    }
}
