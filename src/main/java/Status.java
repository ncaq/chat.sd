package net.ncaq.chat.sd;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;
import lombok.*;
import static net.ncaq.chat.sd.util.Status.*;

/**
 * 通信のステータス.
 */
@Data
public enum Status {
    LOGIN_SUCCEED   (  0, "login succeed"),
    PASSWORD_INVALID(100, "password invalid"),
    MULTIPLE_LOGIN  (101, "multiple login"),
    LOGOUT_SUCCEED  (600, "logout succeed (extension)"),
    MULTIPLE_LOGOUT (601, "multiple logout (extension)");

    private Status(final Integer code, final String description){
        this.code = code;
        this.description = description;
    }

    private final Integer code;
    private final String description;

    /**
     * @return フルレスポンス文字列
     */
    @Override
    public String toString() {
        return this.code.toString() + " " + this.description;
    }

    /**
     * @param code 3桁までのステータスコード
     */
    public static Status of(final Integer code) {
        return codeTable.get(code);
    }

    /**
     * @param response フルレスポンス文字列
     */
    public static Status of(final String response) {
        System.err.println(response);
        final Matcher m = Pattern.compile("[^\\d]*(\\d+).*").matcher(response);
        m.matches();
        return Status.fromCode(Integer.parseInt(m.group(1)));
    }

    private final static Map<Integer, Status> codeTable = new HashMap<Integer, Status>() {
        {
            Arrays.stream(Status.values()).forEach(s -> put(s.getCode(), s));
        }
    };
}
