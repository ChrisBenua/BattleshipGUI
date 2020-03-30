package battleship.gui;

import battleship.inner.IBattleshipGame;
import battleship.inner.ISubscriber;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main Container of all UI elements
 */
public class RootPane extends GridPane
        implements IRefreshable<RootPane.RefreshValues>, ISubscriber<RootPane.GameEvents> {
    private OceanGridPane oceanGridPane;
    private IBattleshipGame battleshipGame;
    private GameStatsContainer container;
    private IEndGameReactor application;

    public void setChildren(OceanGridPane oceanGridPane, EventLogsContainer eventLogsContainer,
                            GameStatsContainer gameStatsContainer) {
        this.getChildren().clear();
        this.oceanGridPane = oceanGridPane;
        this.container = gameStatsContainer;
        var columnConstraint1 = new ColumnConstraints();
        columnConstraint1.setPercentWidth(50);
        //columnConstraint1.setHgrow(Priority.ALWAYS);
        var columnConstraint2 = new ColumnConstraints();
        columnConstraint2.setPercentWidth(50);
        this.getColumnConstraints().addAll(columnConstraint1, columnConstraint2);

        var rowConstraint1 = new RowConstraints();
        rowConstraint1.setPercentHeight(65);
        //rowConstraint1.setVgrow(Priority.ALWAYS);
        var rowConstraint2 = new RowConstraints();
        rowConstraint2.setPercentHeight(35);
        this.getRowConstraints().addAll(rowConstraint1, rowConstraint2);

        this.add(oceanGridPane, 0, 0);
        this.add(eventLogsContainer, 0, 1, 2, 1);
        this.add(container, 1, 0);
        //this.getChildren().add(oceanGridPane);
        //this.oceanGridPane.setAlignment(Pos.CENTER_LEFT);
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
        this.oceanGridPane.setStates(battleshipGame.getOceanState());

        this.container.setSunkShips(this.battleshipGame.getShipsSunk());
        this.container.setTotalShots(this.battleshipGame.getShotsFired());
        this.container.setDamagedShips(this.battleshipGame.getDamagedShipsCount());
        this.container.setUntouchedShips(this.battleshipGame.getUntouchedShipsCount());
    }

    /**
     * Handles game events
     * @param newVal Game event(End Of Game or start new game)
     */
    @Override
    public void accept(GameEvents newVal) {
        if (newVal == GameEvents.END_OF_GAME) {
            var pane = new StackPane(new EndOfGamePane(battleshipGame.getShotsFired(), () -> {
                application.restart();
            }, () -> {
                application.quit();
            }));
            Scene scene = new Scene(pane,400, 200);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } else {
            NotificationManager.showNotification("You can't hit at one point two times!", 2500, this);
        }
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
