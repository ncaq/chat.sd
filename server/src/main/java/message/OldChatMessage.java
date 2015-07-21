package net.ncaq.chat.sd.server.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * チャットのログ形式
 */
@Entity
public class OldChatMessage extends ChatMessage {
    @Override
    public String type() {
        return "oldchat";
    }

    @Override
    public String toTimeLine() {
        return String.join(" ", new String[]{this.type(), getPoster().getName(), getId().toString(), messageDateFormat.format(getSubmit()), getBody()}).trim();
    }

    public static OldChatMessage of(final ChatMessage m) {
        val result = new OldChatMessage();
        result.setSubmit(m.getSubmit());
        result.setPoster(m.getPoster());
        result.setBody(m.getBody());
        return result;
    }
}
