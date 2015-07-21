package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import lombok.*;

/**
 * メッセージを規約通りに整形する.
 */
public class MessageTranslation {
    public static String toJapanese(final String message) {
        final Matcher m = Pattern.compile("(\\S+).*").matcher(message);
        m.matches();
        val p = MessagePattern.of(m.group(1));
        if(p == null) {
            return message;
        }
        else {
            return p.getPattern().matcher(message).replaceAll(p.getReplace());
        }
    }
}

enum MessagePattern {
    Chat("chat", "chat (\\S+) (\\S+)", "$1さん>$2"),
    Login("login", "login user (\\S+) (\\S+ \\S+)", "$1さんがログインしました。前回のログインは$2です"),
    Logout("logout", "logout user (\\S+) (\\S+ \\S+) (\\d+)", "$1さんがログアウトしました。$2にログインして以来,$3個の発言をしました。");

    MessagePattern(final String type, final String patternStr, final String replace) {
        this.type = type;
        this.pattern = Pattern.compile(patternStr);
        this.replace = replace;
    }

    public static MessagePattern of(final String type) {
        return typeTable.get(type);
    }

    private final static Map<String, MessagePattern> typeTable = new HashMap<String, MessagePattern>() {
        {
            Arrays.stream(MessagePattern.values()).forEach(p -> put(p.getType(), p));
        }
    };

    @Getter
    private final String type;
    @Getter
    private final Pattern pattern;
    @Getter
    private final String replace;
}
