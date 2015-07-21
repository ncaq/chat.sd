package net.ncaq.chat.sd.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;

/**
 * チャットのログ形式
 */
@Entity
public class OldMessage extends ChatMessage {
    @Override
    public String type() {
        return "oldchat";
    }

    @Override
    public String toTimeLineBody() {
        return String.join(" ", new String[]{getPoster().getName(), getId().toString(), messageDateFormat.format(getSubmit()), getBody()});
    }

    public static OldChatMessage of(final ChatMessage m) {
        val result = new OldChatMessage();
        result.setSubmit(m.getSubmit());
        result.setPoster(m.setPoster());
        result.setBody(m.getBody());
        return result;
    }
}
