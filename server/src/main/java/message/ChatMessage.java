package net.ncaq.chat.sd.server.message;

import java.sql.*;
import java.util.regex.*;
import javax.persistence.*;
import lombok.*;

/**
 * チャットによる会話メッセージ.
 * oldchat形式もサポートします.
 */
@Entity
public class ChatMessage extends Message {
    @Override
    public String toTimeLine() {
        return String.join(" ", new String[]{"chat", getPoster().getName(), getBody()}).trim();
    }

    public String toOldTimeLine() {
        return String.join(" ", new String[]{"oldchat", getPoster().getName(), getId().toString(), messageDateFormat.format(getSubmit()), getBody()}).trim();
    }
}
