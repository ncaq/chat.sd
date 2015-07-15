package net.ncaq.chat.sd.server;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import javax.persistence.*;

public class MessageLog {
    public MessageLog() {

    }

    public void write(final String newMessage) {

    }

    /**
     * 消す予定.
     */
    @Deprecated
    public static MessageLog getInstance() {
        return new MessageLog();
    }
}
