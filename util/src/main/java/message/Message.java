package net.ncaq.chat.sd.util.message;

import java.sql.*;
import javax.persistence.*;
import lombok.*;
import net.ncaq.chat.sd.util.*;

/**
 * 汎用メッセージ.
 */
@Data
@Entity
public abstract class Message {
    /** 自動生成id. */
    @Id
    private Long id;

    /** メッセージの作成時間. */
    @Temporal(TemporalType.DATE)
    private Date create = new java.sql.Date(System.currentTimeMillis());

    /** メッセージの投稿者. */
    @EmbeddedId
    private User poster;

    /** メッセージ本文. */
    private String body;

    /**
     * タイムライン向け通知.
     */
    public String notify() {
        return this.messageType() + " " + this.notifyBody();
    }

    /**
     * 通知Body.
     */
    public String notifyBody();

    public static Message fromCode(final String statusCode) {
    }

    public static Message fromResponse(final String statusResponse) {
        final Matcher m = Pattern.compile("[^\\d]*(\\d+).*").matcher(statusResponse);
        m.matches();
        return Message.fromCode(Integer.parseInt(m.group(1)));
    }

    @Transient
    private static final Map<Integer, Message> children = new {
        // ClassLoader.
    };

    /**
     * 通信ステータス.
     */
    public static String status() {
        return String.join(" ", {this.code.toString(), this.messageType(), this.description()});
    }

    /**
     * メッセージの種類.
     * これで識別します.
     * 通知やステータスの説明に繋げます.
     */
    abstract public static String messageType();

    /**
     * 通信ステータス識別コード.
     */
    abstract public static Integer code();
    /**
     * 通信ステータス説明.
     */
    abstract public static String description();
}
