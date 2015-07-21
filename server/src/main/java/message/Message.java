package net.ncaq.chat.sd.server.message;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import javax.persistence.*;
import lombok.*;
import net.ncaq.chat.sd.*;

/**
 * 汎用メッセージ.
 */
@Data
@Entity
public abstract class Message {
    /** 自動生成id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** メッセージの投稿時間. */
    @Temporal(TemporalType.TIMESTAMP)
    private Date submit = new Date(System.currentTimeMillis());

    /** メッセージの投稿者. */
    private User poster;

    /** メッセージ本文. */
    private String body;

    /**
     * メッセージの種類表示.
     * 通知やステータスの説明に繋げます.
     */
    abstract public String type();

    /**
     * タイムライン向け通知.
     */
    abstract public String toTimeLine();

    /**
     * このクラスで使う日付表現.
     */
    @Transient
    protected final DateFormat messageDateFormat = new SimpleDateFormat("yyyy/LL/dd HH:mm:ss");
}
