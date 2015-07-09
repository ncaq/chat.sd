package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
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

public class Main extends Application implements Initializable {
    public static void main(final String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Main.fxml"))));
        stage.show();
    }

    @FXML
    public void initialize(final URL location, final ResourceBundle resources) {
        this.timeline.setItems(FXCollections.<HBox>observableArrayList());

        final TextInputDialog requestRemoteHost = new TextInputDialog();
        requestRemoteHost.setHeaderText("please input remote host");
        try {
            this.connector = new Connector(InetAddress.getByName(requestRemoteHost.showAndWait().orElse("localhost")));
        }
        catch(final IOException err) { // 接続できなければエラーアラートを表示して終了します
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Error");
            alert.setContentText(err.toString() + "\n" + "終了します");

            Platform.runLater(() -> {
                    alert.showAndWait();
                    Platform.exit();
                });
        }

        this.messageInput.setOnKeyPressed(this::messageInputKeybinding);
        this.messageSubmit.setOnAction(this::send);
        Platform.runLater(() -> messageInput.requestFocus());

        Thread t = new Thread(() -> {
                for(;;) {
                    receive();
                }});
        t.setDaemon(true);      // ウインドウ閉じたら強制終了
        t.start();
    }

    private void messageInputKeybinding(final KeyEvent key) {
        switch(key.getCode()) {
        case ENTER:
            this.send(null);
        }
    }

    public void send(final ActionEvent evt) {
        try {
            this.connector.writeln(this.messageInput.getText());
            this.messageInput.clear();
        }
        catch(final IOException err) {
            System.err.println(err);
        }
    }

    public void receive() {
        try {
            final String l = this.connector.readLine();
            Platform.runLater(() -> this.timeline.getItems().add(new HBox(new Label(l)))); // runLaterでJavaFXのスレッドで実行させないと例外
        }
        catch(final IOException err) {
            System.err.println(err);
        }
    }

    @FXML
    private ListView<HBox> timeline;
    @FXML
    private TextField messageInput;
    @FXML
    private Button messageSubmit;

    private Connector connector;
}
