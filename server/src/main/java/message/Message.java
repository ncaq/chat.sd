package net.ncaq.chat.sd.server.message;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import javax.persistence.*;
import lombok.*;
import net.ncaq.chat.sd.server.*;

/**
 * 抽象メッセージ型.
 */
@Data
@MappedSuperclass
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

    /**
     * タイムライン向け表現.
     */
    abstract public String toTimeLine();

    /**
     * このクラスで使う日付表現.
     * SimpleDateFormatはスレッドセーフではありませんので,インスタンスごとに保持するのが簡単な対策です.
     */
    @Transient
    protected final DateFormat messageDateFormat = new SimpleDateFormat("yyyy/LL/dd HH:mm:ss");
}
