package net.ncaq.chat.sd.message;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.jar.*;
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
     * 通信ステータスコードからメッセージを構築.
     * @param statusCode 3桁までのコード
     * @return Messageを実装したインスタンス
     */
    public static Message fromCode(final Integer statusCode) throws InstantiationException, IllegalAccessException {
        return codeTable.get(statusCode).newInstance();
    }

    /**
     * messageパッケージのクラスを収集します.
     * リフレクション黒魔術.
     */
    @Transient
    private static final Map<Integer, Class<? extends Message>> codeTable = new HashMap<Integer, Class<? extends Message>>() {
        {
            try {
                final Class<Message> self = Message.class;
                final URL u = self.getResource("");
                final ClassLoader cl = ClassLoader.getSystemClassLoader();
                final List<String> fileNames = (u.getProtocol().equals("jar")) ?
                ((JarURLConnection)u.openConnection()).getJarFile().stream().map(j -> j.getName()).collect(Collectors.toList()) :
                Arrays.stream((new File(u.getFile())).listFiles()).map(f -> f.getAbsolutePath()).collect(Collectors.toList());

                final List<String> classNames = fileNames.stream().map(f -> f.replaceAll("/", "\\.").replaceAll(".*net\\.ncaq", "net.ncaq")).collect(Collectors.toList());
                for(final String c : classNames) {
                    if(c.startsWith("net.ncaq.chat.sd.message") && c.endsWith(".class")) {
                        try {
                            final Class<? extends Message> child = (Class<Message>)cl.loadClass(c.replaceAll("\\.class$", ""));
                            final Integer childCode = (Integer)child.getMethod("code").invoke(child.newInstance());
                            this.put(childCode, child);
                        }
                        catch(InstantiationException|NoSuchMethodException exp) {
                            // abstractのインスタンス作成,メソッド無しは黙殺する
                        }
                    }
                }
            }
            catch(final Exception exp) {
                System.err.println(exp);
            }
        }
    };

    /**
     * 通信ステータス文字列からメッセージを構築.
     * @return Messageを実装したインスタンス
     */
    public static Message fromStatus(final String statusStatus) throws InstantiationException, IllegalAccessException {
        final Matcher m = Pattern.compile("[^\\d]*(\\d+).*").matcher(statusStatus);
        m.matches();
        return Message.fromCode(Integer.parseInt(m.group(1)));
    }

    /**
     * タイムライン向け通知.
     */
    public String forTimeLine() {
        return String.join(" ", new String[]{this.type(), this.forTimeLineBody()}).trim();
    }

    /**
     * このクラスで使う日付表現.
     */
    protected static final DateFormat messageDateFormat = new SimpleDateFormat("yyyy/LL/dd HH:mm:ss");

    /**
     * 通信ステータス.
     */
    public String status() {
        return String.join(" ", new String[]{this.code().toString(), this.type(), this.description()}).trim();
    }

    /**
     * メッセージの種類.
     * これで識別します.
     * 通知やステータスの説明に繋げます.
     */
    abstract public String type();

    /**
     * 通信ステータス識別コード.
     */
    abstract public Integer code();
    /**
     * 通信ステータス説明.
     */
    abstract public String description();

    /**
     * 通知Body.
     */
    abstract public String forTimeLineBody();
}
