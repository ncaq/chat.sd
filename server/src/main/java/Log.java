package net.ncaq.chat.sd.server;

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
        this.chat = coNotFinal;

        this.makeTableWhenNothing();
    }

    private void makeTableWhenNothing() {
        try {
            if(!this.chat.getMetaData().getTables(null, null, "log", null).next()){ // log table don't exist
                this.chat.prepareStatement("create table log(" +
                                           "id integer primary key not null," +
                                           "utc timestamp default current_timestamp not null," +
                                           "message text not null" +
                                           ")").executeUpdate();
            }
        }
        catch(final SQLException err) {
            System.err.println(err);
        }
    }

    public void write(final String message) {
        this.single.execute(() -> {
                try {
                    final PreparedStatement st = chat.prepareStatement("insert into log(message) values(?)");
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

    private final Connection chat;
    private final ExecutorService single = Executors.newSingleThreadExecutor();
}
