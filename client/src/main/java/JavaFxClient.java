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
import net.ncaq.chat.sd.*;

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
                this.connector = new Connector(l.getHostname(), l.getUsername(), l.getPassword(), this::receive);
            }
            catch(final ConnectException exc) {
                exc.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(exc.toString());
                alert.showAndWait();
            }
            catch(final Exception exc) {
                exc.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText(exc.toString());
                alert.showAndWait();
                System.exit(-1);
            }
        }
        while(this.connector == null);

        this.message.setOnAction(this::send);
        this.submit.setDefaultButton(true);

        Platform.runLater(() -> {
                message.requestFocus();
            });
    }

    public void send(final ActionEvent evt) {
        try {
            this.connector.writeln(this.message.getText());
            this.message.clear();
        }
        catch(IOException|InterruptedException err) {
            System.err.println(err);
        }
    }

    public void receive(final String newMessage) {
        Platform.runLater(() -> { // runLaterでJavaFXのスレッドで実行させないと例外
                Boolean scrollWasEnd = false; // スクロールが最後まで達していたか
                for(final Node n : timeline.lookupAll(".scroll-bar")) {
                    if(n instanceof ScrollBar) {
                        final ScrollBar s = (ScrollBar)n;
                        if(s.getOrientation() == Orientation.VERTICAL && s.getMax() == s.getValue()) {
                            scrollWasEnd = true;
                        }
                    }
                }
                this.timeline.getItems().add(new HBox(new Label(newMessage)));
                if(scrollWasEnd) {
                    this.timeline.scrollTo(Integer.MAX_VALUE);
                }
            });
    }

    @FXML
    private ListView<HBox> timeline;
    @FXML
    private TextField message;
    @FXML
    private Button submit;

    private Connector connector;
}
