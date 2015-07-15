package net.ncaq.chat.sd.server;

import java.sql.*;
import java.util.*;
import javax.persistence.*;
import lombok.*;

/**
 * チャットメッセージ.
 */
@Data
@Entity
public class Message {
    @Id
    private Long id;

    private MessageType type;

    private String body;

    @Temporal(TemporalType.DATE)
    private java.sql.Date create = new java.sql.Date(System.currentTimeMillis());

    @EmbeddedId
    private User submitUser;
}
