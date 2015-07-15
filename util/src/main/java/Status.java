package net.ncaq.chat.sd.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import static net.ncaq.chat.sd.util.Status.*;

/**
 * 通信のステータスコード.
 */
public enum Status {
    LOGIN_SUCCEED   (  0, "login succeed"             , "ログイン成功"),
    PASSWORD_INVALID(100, "password invalid"          , "パスワードが間違っています"),
    MULTIPLE_LOGIN  (101, "multiple login"            , "既にログインしています"),
    LOGOUT_SUCCEED  (600, "logout succeed(extension)" , "ログアウト成功(テスト用)"),
    MULTIPLE_LOGOUT (601, "multiple logout(extension)", "ログインしていません(テスト用)");

    private Status(final Integer code, final String message, final String description){
        this.code = code;
        this.message = message;
        this.description = description;
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
        return this.description;
    }

    /**
     * @return フルレスポンス文字列
     */
    @Override
    public String toString() {
        return this.code.toString() + " " + this.message;
    }

    /**
     * @param code 3桁までのステータスコード
     */
    public static Status fromCode(final Integer code) {
        return codeTable.get(code);
    }

    /**
     * @param response フルレスポンス文字列
     */
    public static Status fromResponse(final String response) {
        System.err.println(response);
        final Matcher m = Pattern.compile("[^\\d]*(\\d+).*").matcher(response);
        m.matches();
        return Status.fromCode(Integer.parseInt(m.group(1)));
    }

    private final Integer code;
    private final String message;
    private final String description;

    private final static Map<Integer, Status> codeTable = new HashMap<Integer, Status>() {
        {
            Arrays.stream(Status.values()).forEach(s -> put(s.getCode(), s));
        }
    };
}
