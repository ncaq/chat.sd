package net.ncaq.chat.sd.server.message;

import java.sql.*;
import java.util.regex.*;
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
    public String toTimeLine() {
        return String.join(" ", new String[]{this.type(), getPoster().getName(), getBody()}).trim();
    }

    public String toOldTimeLine() {
        return String.join(" ", new String[]{"oldchat", getPoster().getName(), getId().toString(), messageDateFormat.format(getSubmit()), getBody()}).trim();
    }
}
