package net.ncaq.chat.sd.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class StatusCode {
    public StatusCode(final Integer code) {
        this.code = code;
    }

    // public StatusCode(final String full) {

    // }

    private final Integer code;

    private final static Map<Integer, String> codeMessage = new ConcurrentHashMap<Integer, String>(){{
            put(0, "login succeed");
            put(100, "password invalid");
            put(101, "multiple login");
        }};
}
