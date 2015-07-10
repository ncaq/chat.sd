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
import net.ncaq.chat.sd.util.*;

/**
 * ログイン用ダイアログ.
 * Dialogの機能が本格的に使用できるのはJDK9からなので,その場凌ぎが多いです
 */
public class LoginDialog extends Dialog<Pair<String, User>> {
    public LoginDialog() {
        this.setTitle("Login Dialog");

        final ButtonType loginButtonType = new ButtonType("Login");
        this.getDialogPane().getButtonTypes().add(loginButtonType);

        final VBox input = new VBox();
        final TextField hostname = new TextField();
        hostname.setPromptText("localhost");
        final TextField username = new TextField();
        username.setPromptText("anonymous");
        final PasswordField password = new PasswordField();
        password.setPromptText("");
        input.getChildren().addAll(new HBox(new Label("Hostname: "), hostname),
                                   new HBox(new Label("Username: "), username),
                                   new HBox(new Label("Password: "), password));

        this.getDialogPane().setContent(input);

        this.setResultConverter(dialogButton -> {
                try {
                    return new Pair<>(hostname.getText(), new User(username.getText(), password.getText()));
                }
                catch(final Exception err) {
                    return null;
                }});
    }
}
