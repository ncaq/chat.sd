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
public class MessageTranslator {
    /**
     * 人間が読むような形式に変換します.
     * @param lang 言語選択ですが,現在は機能していません."ja"限定です.
     * @param message 変換対象のメッセージ.
     */
    public static String toHuman(final String lang, final String message) {
        final Matcher m = Pattern.compile("(\\S+).*").matcher(message);
        m.matches();
        val p = MessagePattern.of(m.group(1));
        if(p == null || !lang.equals("ja")) {
            return message;
        }
        else {
            return p.getPattern().matcher(message).replaceAll(p.getReplace());
        }
    }
}

enum MessagePattern {
    Chat("chat", "chat\\s*(\\S+)\\s*(\\S*)", "$1さん>$2"),
    OldChat("oldchat", "oldchat\\s*(\\S+)\\s*(\\d+)\\s*(\\S+ \\S+)\\s*(\\S*)", "$2: $1さん>$4 ($3)"),
    Login("login", "login\\s*user\\s*(\\S+)\\s*(\\S*\\s*\\S*)", "$1さんがログインしました。前回のログイン: $2"),
    Logout("logout", "logout\\s*user\\s*(\\S+)\\s*(\\S+ \\S+)\\s*(\\d+)", "$1さんがログアウトしました。$2にログインして以来,$3個の発言をしました。"),
    CurUser("curuser", "curuser\\s*(\\d+)\\s*(.*)", "ログイン数: $1, ユーザ: $2");

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
