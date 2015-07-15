package net.ncaq.chat.sd.util.message;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.jar.*;
import java.util.regex.*;
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
    private java.sql.Date create = new java.sql.Date(System.currentTimeMillis());

    /** メッセージの投稿者. */
    @EmbeddedId
    private User poster;

    /** メッセージ本文. */
    private String body;

    /**
     * タイムライン向け通知.
     */
    public String forTimeLine() {
        return this.messageType() + " " + this.forTimeLineBody();
    }

    /**
     * 通知Body.
     */
    public String forTimeLineBody() {
        return "";
    }

    public static Message fromCode(final Integer statusCode) {
        try {
            return codeTable.get(statusCode).newInstance();
        }
        catch(InstantiationException|IllegalAccessException err) {
            System.err.println(err);
            return null;
        }
    }

    @Transient
    private static final Map<Integer, Class<? extends Message>> codeTable = new HashMap<Integer, Class<? extends Message>>() {
        {
            try {
                final String packagePath = "net/ncaq/chat/sd/util/message";
                final ClassLoader cl = ClassLoader.getSystemClassLoader();
                final Enumeration<JarEntry> eje = ((JarURLConnection)cl.getResource(packagePath).openConnection()).getJarFile().entries();
                while(eje.hasMoreElements()) {
                    final String filepath = eje.nextElement().getName();
                    if(filepath.startsWith(packagePath) && filepath.endsWith(".class")) {
                        final Class<? extends Message> messageChild = (Class<Message>)cl.loadClass(filepath.replace('/', '.'));
                        final Method m = messageChild.getMethod("code");
                        final Integer childCode = (Integer)m.invoke(null);
                        this.put(childCode, messageChild);
                    }
                }
            }
            catch(final Exception err) {
                System.err.println(err);
                System.exit(-1);
            }
        }
    };

    public static Message fromResponse(final String statusResponse) {
        final Matcher m = Pattern.compile("[^\\d]*(\\d+).*").matcher(statusResponse);
        m.matches();
        return Message.fromCode(Integer.parseInt(m.group(1)));
    }

    /**
     * 通信ステータス.
     */
    public String status() {
        return String.join(" ", new String[]{this.code().toString(), this.messageType(), this.description()});
    }

    /**
     * メッセージの種類.
     * これで識別します.
     * 通知やステータスの説明に繋げます.
     */
    abstract public String messageType();

    /**
     * 通信ステータス識別コード.
     */
    abstract public Integer code();
    /**
     * 通信ステータス説明.
     */
    abstract public String description();
}
