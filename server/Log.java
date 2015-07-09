package net.ncaq.chat.server;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

public class Log {
    private Log() {
        Connection coNotFinal = null;
        try {
            Class.forName("org.sqlite.JDBC");
            coNotFinal = DriverManager.getConnection("jdbc:sqlite:chat.sqlite3");
        }
        catch(final Exception err) { // この例外は回復諦めて終了します
            System.err.println(err);
            System.exit(-1);
        }
        this.co = coNotFinal;
    }

    public void write(final String message) {
        this.single.execute(() -> {
                try {
                    final PreparedStatement st = co.prepareStatement("insert into chat(message) values(?)");
                    st.setString(1, message);
                    st.executeUpdate();
                }
                catch(final SQLException err) {
                    System.err.println(err);
                }});
    }

    public static synchronized Log getInstance() {
        return self == null ? self = new Log() :
            self;
    }

    private static Log self;

    private final Connection co;
    private final ExecutorService single = Executors.newSingleThreadExecutor();
}
