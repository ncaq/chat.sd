package net.ncaq.chat.sd.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

/**
 * 通信のステータスコード.
 * よく考えたら列挙体で実装するべきだった.
 */
public class StatusCode {
    /**
     * @param code コード番号
     */
    public StatusCode(final Integer code) {
        this.code = code;
    }

    /**
     * @param full フルメッセージ文字列
     */
    public StatusCode(final String full) {
        final Matcher m = Pattern.compile("^\\d+").matcher(full);
        m.find();
        this.code = Integer.parseInt(m.group());
    }

    /**
     * @return 生コード
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * @return 説明
     */
    public String getDescription() {
        return description.get(this.code);
    }

    /**
     * @return フルメッセージ文字列
     */
    @Override
    public String toString() {
        return this.code.toString() + " " + message.get(this.code);
    }

    /**
     * @return codeが等しければ同一
     */
    @Override
    public boolean equals(final Object take) {
        return take instanceof StatusCode ? this.getCode().equals(((StatusCode)take).getCode()) :
            false;
    }

    /**
     * @return hashCode == code
     */
    @Override
    public int hashCode() {
        return this.getCode();
    }

    private final Integer code;

    private final static Map<Integer, String> message = new ConcurrentHashMap<Integer, String>(){{
            put(0, "login succeed");
            put(100, "password invalid");
            put(101, "multiple login");
            put(600, "logout succeed(extension)");
            put(601, "multiple logout(extension)");
        }};

    private final static Map<Integer, String> description = new ConcurrentHashMap<Integer, String>(){{
            put(0, "ログイン成功");
            put(100, "パスワードが間違っています");
            put(101, "既にログインしています");
            put(600, "ログアウト成功(テスト用)");
            put(601, "ログインしていません(テスト用)");
        }};
}
