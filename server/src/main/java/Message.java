package net.ncaq.chat.sd.server;

import java.sql.*;
import java.util.*;
import javax.persistence.*;

@Entity
public class Message {
    @Id
    private Long id;

    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp create;

    @EmbeddedId
    private User submitUser;
}
