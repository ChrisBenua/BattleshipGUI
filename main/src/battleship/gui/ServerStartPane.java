package battleship.gui;

import battleship.inner.IBattleshipGame;
import battleship.network.dto.IOnConnectionHandler;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles successful connection event
 */
interface IBeforeGreetingHandler {
    void showGreetingPaneIn(Stage stage);
}

/**
 * Pane for starting server
 */
public class ServerStartPane extends GridPane implements IOnConnectionHandler {
    /**
     * TextField for entering port
     */
    private TextField portTextField;
    /**
     * IBattleshipGame where should set IClientServer
     */
    IBattleshipGame game;
    /**
     * Assembly for getting IServer
     */
    battleship.network.Assembly networkAssembly;
    /**
     * Gets notified successful connections
     */
    IBeforeGreetingHandler beforeGreetingHandler;

    public ServerStartPane(IBattleshipGame game, IBeforeGreetingHandler beforeGreetingHandler, battleship.network.Assembly networkAssembly) {
        this.game = game;
        this.networkAssembly = networkAssembly;
        this.beforeGreetingHandler = beforeGreetingHandler;

        performLayout();
    }

    void performLayout() {
        portTextField = new TextField();
        portTextField.setPromptText("Введите порт:");

        var submitButton = new Button("Старт");
        submitButton.setMaxWidth(Double.MAX_VALUE);
        submitButton.setOnMouseClicked(this::submitButtonClickHandler);

        for (int i = 0; i < 5; ++i) {
            var colConstraint = new ColumnConstraints();
            colConstraint.setFillWidth(true);
            colConstraint.setHgrow(Priority.ALWAYS);
            colConstraint.setPercentWidth(20);
            this.getColumnConstraints().add(colConstraint);
        }
        for (int i = 0; i < 2; ++i) {
            var rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(50);
            this.getRowConstraints().add(rowConstraint);
        }

        this.add(portTextField, 1, 0, 3, 1);
        GridPane.setFillWidth(submitButton, true);
        GridPane.setRowIndex(submitButton, 1);
        GridPane.setColumnIndex(submitButton, 2);
        this.getChildren().add(submitButton);
    }

    public void submitButtonClickHandler(MouseEvent event) {
        try {
            int port = Integer.parseInt(portTextField.getText());
            if (port > 0 && port < 65536) {
                var server = networkAssembly.getBattleshipServer();
                game.setClientServer(server);
                server.addOnConnectionHandler(this);

                server.runClientServer(port);
                NotificationManager.showNotification("Started server, looking for opponents", 2000, this);
            }
        } catch (NumberFormatException e) {
            NotificationManager.showNotification("Invalid format for port number", 1500, this);
        } catch (IOException e) {
            NotificationManager.showNotification(e.getMessage(), 2000, this);
        }
    }

    @Override
    public void onSuccessfulConnection() {
        Platform.runLater(() -> {
            NotificationManager.showNotification("Connected successfully!", 1500, this);
            var capturedThis = this;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        beforeGreetingHandler.showGreetingPaneIn((Stage)capturedThis.getScene().getWindow());
                    });
                }
            }, 2000);
        });
    }

    @Override
    public void onFailedConnection(String error) {
        Platform.runLater(() -> {
            NotificationManager.showNotification("Connection failed." + error, 2000, this);
        });

    }
}
