package net.ncaq.chat.sd.server;

import java.sql.*;
import java.util.*;
import javax.persistence.*;

/**
 * メッセージの種類.
 */
@Entity
public enum MessageType {
    /** 制御 */
    CONTROL,
    /** 会話 */
    CHAT;
}
