package net.ncaq.chat.sd.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
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

public class JavaFxClient extends Application {
    @Override
    public void start(final Stage stage) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/Main.fxml"))));
        stage.show();
    }

    @FXML
    public void initialize() {
        this.timeline.setItems(FXCollections.<HBox>observableArrayList());

        do {
            try {
                final LoginDialog l = new LoginDialog();
                l.showAndWait();
                this.connector = new Connector(l.getHostname(), l.getUsername(), l.getPassword());
            }
            catch(final Exception err) {
                System.err.println(err);

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(err.getMessage());
                alert.showAndWait();
            }
        }
        while(this.connector == null);

        this.message.setOnAction(this::send);
        this.submit.setDefaultButton(true);
        Platform.runLater(() -> message.requestFocus());

        Thread t = new Thread(() -> {
                for(;;) {
                    receive();
                }});
        t.setDaemon(true);      // ウインドウ閉じたら強制終了
        t.start();
    }

    public void send(final ActionEvent evt) {
        try {
            this.connector.writeln(this.message.getText());
            this.message.clear();
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
    private TextField message;
    @FXML
    private Button submit;

    private Connector connector;
}
