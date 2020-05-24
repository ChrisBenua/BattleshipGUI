package battleship.gui;

import battleship.inner.IBattleshipGame;
import battleship.network.IClientServer;
import battleship.network.IDtoEventsHandler;
import battleship.network.dto.GreetingsDto;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class GreetingsPane extends GridPane implements IDtoEventsHandler {
    private battleship.network.Assembly networkAssembly;
    private TextField nameTextField;
    private IClientServer clientServer;
    private Button greetButton;
    private IBattleshipGame battleshipGame;

    public GreetingsPane(IBattleshipGame battleshipGame, IClientServer clientServer, battleship.network.Assembly networkAssembly) {
        this.networkAssembly = networkAssembly;
        this.battleshipGame = battleshipGame;
        networkAssembly.getDtoReader().addEventsHandler(this);

        this.clientServer = clientServer;

        nameTextField = new TextField();
        nameTextField.setPromptText("Введите имя для знакомства");

        greetButton = new Button("Познакомиться");
        greetButton.setMaxWidth(Double.MAX_VALUE);
        greetButton.setStyle("-fx-font-size: 11px");
        greetButton.setOnMouseClicked(this::greetButtonClickHandler);
        greetButton.setDisable(true);
        nameTextField.textProperty().addListener((__, oldValue, newValue) -> {
            if (newValue.length() < 3) {
                greetButton.setDisable(true);
            } else {
                greetButton.setDisable(false);
            }
        });

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

        this.add(nameTextField, 1, 0, 3, 1);
        GridPane.setFillWidth(greetButton, true);
        GridPane.setRowIndex(greetButton, 1);
        GridPane.setColumnIndex(greetButton, 2);
        this.getChildren().add(greetButton);
    }

    public void greetButtonClickHandler(MouseEvent event) {
        nameTextField.setDisable(true);
        greetButton.setDisable(true);
        this.battleshipGame.setPlayerName(nameTextField.getText());
        this.clientServer.write(new GreetingsDto(nameTextField.getText()));
        this.battleshipGame.setPlayerName(nameTextField.getText());

        shouldQuit();
    }

    private void shouldQuit() {
        if (Objects.nonNull(battleshipGame.getOpponentName()) && Objects.nonNull(battleshipGame.getPlayerName())) {
            var capturedThis = this;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        ((Stage) capturedThis.getScene().getWindow()).close();
                    });
                }
            }, 4000);
        }
    }

    @Override
    public void processGreetings(GreetingsDto greetingsDto) {
        this.battleshipGame.setOpponentName(greetingsDto.getName());
        Platform.runLater(() -> {
            NotificationManager.showNotification("Your opponent name is: " + greetingsDto.getName(), 3000, this);
        });
        battleshipGame.setOpponentName(greetingsDto.getName());

        shouldQuit();
    }
}
