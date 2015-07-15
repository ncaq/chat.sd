package net.ncaq.chat.sd.server;

import java.sql.*;
import java.util.*;
import javax.persistence.*;

@Entity
public enum MessageType {
    CONTROL,
    CHAT;
}
