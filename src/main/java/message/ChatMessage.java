package net.ncaq.chat.sd.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * チャットによる会話メッセージ
 */
@Entity
public class ChatMessage extends Message {
    @Override
    public String type() {
        return "chat";
    }

    @Override
    public Integer code() {
        return 200;
    }

    @Override
    public String description() {
        return "ok(extension)";
    }

    @Override
    public String toTimeLineBody() {
        return String.join(" ", new String[]{getPoster().getName(), getBody()});
    }
}
