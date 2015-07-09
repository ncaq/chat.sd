package net.ncaq.chat.sd.util;

import java.util.*;
import java.util.concurrent.*;

public class StatusCode {
    public StatusCode(final Integer code) {
        this.code = code;
    }

    public StatusCode(final String full) {
        this.code = Integer.parseInt(full.substring(0, 2));
    }

    public String toString() {
        return this.code.toString() + " " + message.get(this.code);
    }

    private final Integer code;

    private final static Map<Integer, String> message = new ConcurrentHashMap<Integer, String>(){{
            put(0, "login succeed");
            put(100, "password invalid");
            put(101, "multiple login");
        }};
}
