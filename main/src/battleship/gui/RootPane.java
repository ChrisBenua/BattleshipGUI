package battleship.gui;

import battleship.inner.IBattleshipGame;
import battleship.inner.ISubscriber;
import battleship.network.IDtoEventsHandler;
import battleship.network.dto.CancelGameDto;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;


/**
 * Main Container of all UI elements
 */
public class RootPane extends GridPane
        implements IRefreshable<RootPane.RefreshValues>, ISubscriber<RootPane.GameEvents>, IDtoEventsHandler {

    private OceanGridPane opponentOceanGridPane;
    private IBattleshipGame battleshipGame;
    private OceanGridPane playerOceanGridPane;
    private IEndGameReactor application;
    private Label isMyTurnLabel;

    public void setChildren(OceanGridPane opponentOceanGridPane, EventLogsContainer eventLogsContainer,
                            OceanGridPane playerOceanGridPane) {
        var playerLabel = new Label("Ваше поле");
        var opponentLabel = new Label("Поле оппонента");
        var cancelButton = new Button("Прервать игру");

        this.isMyTurnLabel = new Label("");

        this.getChildren().clear();
        this.opponentOceanGridPane = opponentOceanGridPane;
        this.playerOceanGridPane = playerOceanGridPane;
        var columnConstraint1 = new ColumnConstraints();
        columnConstraint1.setPercentWidth(50);
        var columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setPercentWidth(50);
        this.getColumnConstraints().addAll(columnConstraint1, columnConstraint2);

        var rowConstraint1 = new RowConstraints();
        rowConstraint1.setPercentHeight(65);
        var rowConstraint3 = new RowConstraints();
        rowConstraint3.setPercentHeight(35);
        var rowConstraint2 = new RowConstraints();
        this.getRowConstraints().addAll(rowConstraint1, rowConstraint2, rowConstraint3);

        this.add(opponentOceanGridPane, 1, 0);
        this.add(eventLogsContainer, 0, 2, 2, 1);
        this.add(playerOceanGridPane, 0, 0);

        for (var pane: List.of(opponentOceanGridPane, playerOceanGridPane)) {
            GridPane.setMargin(pane, new Insets(15, 0, 0, 0));
        }


        this.add(cancelButton, 0, 1, 1, 1);
        this.add(playerLabel, 0, 0);
        this.add(opponentLabel, 1, 0);
        this.add(isMyTurnLabel, 0, 0, 2, 1);

        battleshipGame.getGameTurn().addListener((__, old, isMyTurn) -> {
            if (old != isMyTurn) {
                Platform.runLater(() -> {
                    this.updateIsMyTurnLabel(isMyTurn);
                });
            }
        });

        GridPane.setValignment(cancelButton, VPos.CENTER);

        this.updateIsMyTurnLabel(battleshipGame.getGameTurn().get());

        for (var label : List.of(playerLabel, opponentLabel, isMyTurnLabel)) {
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.TOP);
        }

        GridPane.setMargin(isMyTurnLabel, new Insets(-10, 0, 0, 0));

        cancelButton.setOnAction(ev -> {
            this.application.cancel();

            showCancelGameDialog(battleshipGame.getPlayerName());
        });

        this.refresh(RefreshValues.ALL);
    }

    void updateIsMyTurnLabel(IBattleshipGame.GameTurn turn) {
        String text = "";
        if (turn == IBattleshipGame.GameTurn.ME) {
            text = "Ваш ход";
        } else if (turn == IBattleshipGame.GameTurn.OPPONENT) {
            text = "Ход оппонента";
        } else if (turn == IBattleshipGame.GameTurn.END) {
            text = "Игра окончена";
        }
        if (!text.equals(isMyTurnLabel.getText())) {
            this.isMyTurnLabel.setText(text);
        }
    }


    /**
     * Sets event handler for starting new game or quitting app
     * @param reactor events handler
     */
    public void setApplication(IEndGameReactor reactor) {
        this.application = reactor;
    }

    /**
     * Constructs new container with game instance
     * @param battleshipGame
     */
    public RootPane(IBattleshipGame battleshipGame) {
        this.battleshipGame = battleshipGame;
        this.setPadding(new Insets(20, 20, 20, 20));
    }

    /**
     * Updates child components
     * @param key which items to refresh
     */
    @Override
    public void refresh(RefreshValues key) {
        this.opponentOceanGridPane.setStates(battleshipGame.getOpponentOceanState());
        this.playerOceanGridPane.setStates(battleshipGame.getPlayerOceanState());
    }

    /**
     * Handles game events
     * @param newVal Game event(End Of Game or start new game)
     */
    @Override
    public void accept(GameEvents newVal) {
        if (newVal == GameEvents.END_OF_GAME) {
            var pane = new StackPane(
                    new EndOfGamePane(battleshipGame.getWinnerName(),
                            battleshipGame.getPlayerName(),
                            battleshipGame.getOpponentName(),
                            battleshipGame.getPlayerShotsCount(),
                            battleshipGame.getEnemyShotsCount(), () -> {
                        this.battleshipGame.getClientServer().close();
                        application.quit();
                    })
            );

            //TODO
            Platform.runLater(() -> {
                Scene scene = new Scene(pane, 400, 200);
                Stage stage = new Stage();
                var window = this.getScene().getWindow();
                var x = window.getX() + window.getWidth() / 2 - 200;
                var y = window.getY() + window.getHeight() / 2 - 100;
                stage.setX(x);
                stage.setY(y);
                stage.setScene(scene);
                stage.show();
            });
        } else if (newVal == GameEvents.REPEATED_HIT) {
            NotificationManager.showNotification("You can't hit at one point two times!", 2500, this);
        }
    }

    private void showCancelGameDialog(String initiator) {
        Platform.runLater(() -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setContentText(String.format("%s: Прекратить игру! Ок?", initiator));

            dialog.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response ->  {
                this.battleshipGame.getClientServer().close();
                this.application.quit();
            });
        });
    }

    @Override
    public void processCancelGameEvent(CancelGameDto cancelGameDto) {
        showCancelGameDialog(cancelGameDto.getInitiator());
    }

    /**
     * Enum for handling refreshes
     */
    public static enum RefreshValues {
        ALL;
    }

    /**
     * Game Events Enum
     */
    public static enum GameEvents {
        END_OF_GAME, REPEATED_HIT
    }
}
