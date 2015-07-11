package net.ncaq.chat.sd.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

/**
 * 通信のステータスコード.
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
        return (take instanceof StatusCode) ? this.getCode().equals(((StatusCode)take).getCode()) :
            false;
    }

    private final Integer code;

    private final static Map<Integer, String> message = new ConcurrentHashMap<Integer, String>(){{
            put(0, "login succeed");
            put(100, "password invalid");
            put(101, "multiple login");
        }};
}
