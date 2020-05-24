package battleship.gui;

import battleship.inner.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;

import java.io.Serializable;
import java.util.List;

class ShipDragBoardDto implements Serializable {
    int shipLength;
    boolean isHorizontal;

    public ShipDragBoardDto(int shipLength, boolean isHorizontal) {
        this.shipLength = shipLength;
        this.isHorizontal = isHorizontal;
    }
}

interface IShipsContainerButtonsClickHandler {
    void onFindOpponentButtonClicked();

    void onPlayButtonClicked(List<Ship> ships);
}

interface IOnFindOpponentWindowCloseHandler {
    void onOpponentWindowClosed();
}

public class ShipsPlacingPane extends GridPane
        implements IShipsContainerButtonsClickHandler,
        IOnFindOpponentWindowCloseHandler {
    private ShipsContainerPane shipsContainer;
    public static final DataFormat shipsDataFormat = new DataFormat("ship.custom");
    private PlacingShipsOceanGridPane pane;
    private SimpleBooleanProperty hasOpponentProperty;
    private SimpleBooleanProperty isFindOpponentButtonEnabledProperty = new SimpleBooleanProperty(true);
    private IShipsContainerButtonsClickHandler parent;
    private IBattleshipGame game;

    public ShipsPlacingPane(IBattleshipGame game, IShipFactory shipFactory, SimpleBooleanProperty hasOpponentProperty, IShipsContainerButtonsClickHandler parent) {
        this.hasOpponentProperty = hasOpponentProperty;
        this.parent = parent;
        this.game = game;
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setHgap(20);

        var colConstraint1 = new ColumnConstraints(200);

        var colConstraint2 = new ColumnConstraints();
        //colConstraint.setFillWidth(true);
        //colConstraint2.setMaxWidth(Double.MAX_VALUE);
        colConstraint2.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().addAll(colConstraint1, colConstraint2);

        var adapter = new PlacementAdapter();
        adapter.setWidth(BattleshipGame.OCEAN_SIZE);
        adapter.setHeight(BattleshipGame.OCEAN_SIZE);
        var hitAdapter = new HitAdapterCollection();
        var factory = new HitAdapterFactory();
        pane = new PlacingShipsOceanGridPane(shipFactory, adapter, hitAdapter, factory);
        shipsContainer = new ShipsContainerPane(pane, isPlayButtonEnabledProperty(), this, Bindings.not(isFindOpponentButtonEnabledProperty));

        GridPane.setVgrow(pane, Priority.ALWAYS);

        this.add(pane, 1, 0);

        placeShipsInContainer();

        game.getOpponentReadyStatus().addListener((__, ___, new_) -> {
            if (new_) {
                Platform.runLater(() -> {
                    NotificationManager.showNotification("Opponent is ready", 2000, this);
                });
            }
        });
    }

    private void placeShipsInContainer() {
        GridPane.setMargin(shipsContainer, new Insets(30, 4, 4, 4));
        this.add(shipsContainer, 0, 0);
    }

    public BooleanBinding isPlayButtonEnabledProperty() {
        System.out.println(hasOpponentProperty.get());
        hasOpponentProperty.addListener((obs, old, new_) -> {
            System.out.println("HasOpponentProperty: " + new_.toString());
        });
        return Bindings.not(Bindings.and(hasOpponentProperty, pane.getIsFilledProperty()));
    }

    @Override
    public void onFindOpponentButtonClicked() {
        isFindOpponentButtonEnabledProperty.set(false);
        parent.onFindOpponentButtonClicked();
    }

    @Override
    public void onPlayButtonClicked(List<Ship> shipList) {
        parent.onPlayButtonClicked(pane.getShips());
        this.shipsContainer.disableClearButton();
        this.game.setPlayerReadyStatus(true);

        String message = "Game is starting.";
        if (!game.getOpponentReadyStatus().get()) {
            message += "Waiting for opponent";
        }
        NotificationManager.showNotification(message, 3000, pane);
    }

    @Override
    public void onOpponentWindowClosed() {
        if (!hasOpponentProperty.get()) {
            isFindOpponentButtonEnabledProperty.set(true);
        }
    }
}
