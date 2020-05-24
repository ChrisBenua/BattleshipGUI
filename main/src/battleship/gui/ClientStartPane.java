package battleship.gui;

import battleship.inner.IBattleshipGame;
import battleship.network.Assembly;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * Pane for finding opponents as a client
 */
public class ClientStartPane extends ServerStartPane {
    /**
     * TextField for entering host
     */
    private TextField hostTextField;
    /**
     * TextField for entering port
     */
    private TextField portTextField;

    /**
     * Creates new instance of ClientStartPane
     * @param game IBattleShipGame which clientServer field will be set
     * @param handler notifies when connection was established
     * @param networkAssembly for getting BattleshipClient
     */
    public ClientStartPane(IBattleshipGame game, IBeforeGreetingHandler handler, Assembly networkAssembly) {
        super(game, handler, networkAssembly);
    }


    @Override
    void performLayout() {
        hostTextField = new TextField();
        hostTextField.setPromptText("Введите хост:");
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
        for (int i = 0; i < 3; ++i) {
            var rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(100.0 / 3);
            this.getRowConstraints().add(rowConstraint);
        }

        this.add(portTextField, 1, 1, 3, 1);
        this.add(hostTextField, 1, 0, 3, 1);

        GridPane.setFillWidth(submitButton, true);
        GridPane.setRowIndex(submitButton, 2);
        GridPane.setColumnIndex(submitButton, 2);
        this.getChildren().add(submitButton);
    }

    /**
     * Tries to connect to given host and port
     * @param event mouse click event
     */
    @Override
    public void submitButtonClickHandler(MouseEvent event) {
        try {
            int port = Integer.parseInt(portTextField.getText());
            if (port > 0 && port < 65536) {
                var client = networkAssembly.getBattleshipClient();
                game.setClientServer(client);
                client.addOnConnectionHandler(this);

                client.runClientServer(hostTextField.getText(), port);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
