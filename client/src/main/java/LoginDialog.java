package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;

/**
 * ログイン用ダイアログ.
 * Dialogの機能が本格的に使用できるのはJDK9からなので,その場凌ぎが多いです
 */
public class LoginDialog extends Dialog<Void> {
    public LoginDialog() {
        this.setTitle("Login Dialog");

        this.getDialogPane().getButtonTypes().add(new ButtonType("Login"));

        final VBox input = new VBox();
        input.getChildren().addAll(new HBox(new Label("Hostname: "), hostname),
                                   new HBox(new Label("Username: "), username),
                                   new HBox(new Label("Password: "), password));

        Platform.runLater(() -> hostname.requestFocus());

        this.getDialogPane().setContent(input);
    }

    public String getHostname() {
        return this.hostname.getText();
    }

    public String getUsername() {
        return this.username.getText();
    }

    public String getPassword() {
        return this.password.getText();
    }

    private final TextField hostname = new TextField();
    private final TextField username = new TextField();
    private final PasswordField password = new PasswordField();
}
