package battleship.inner;

import battleship.gui.IRefreshable;
import battleship.gui.RootPane;
import battleship.network.IClientServer;
import battleship.network.IDtoEventsHandler;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.adapter.JavaBeanBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

/**
 * Interfaces for defining main operations with game
 */
public interface IBattleshipGame extends IDtoEventsHandler {
    /**
     * Places ships randomly, validating their position
     */
    void placeShipsRandomly();

    void addOnUpdateField(IRefreshable<RootPane.RefreshValues> onUpdateOpponentField);

    void shootAtOpponentField(int row, int column);

    void setOpponentReadyStatus(boolean status);

    ObservableBooleanValue getOpponentReadyStatus();

    void setPlayerReadyStatus(boolean status);

    ObservableBooleanValue getPlayerReadyStatus();

    boolean isMyTurn();

    ReadOnlyBooleanProperty getIsMyTurn();

    ReadOnlyObjectProperty<GameTurn> getGameTurn();

    void setTurn(GameTurn turn);

    void setMyTurn(boolean turn);

    /**
     * Sets game events subscriber
     * @param subscriber
     */
    void setSubscriber(ISubscriber<RootPane.GameEvents> subscriber);

    /**
     * Gets amount of shots fired
     * @return amount of shots fired
     */
    int getShotsFired();

    /**
     * Gets amount of hits made
     * @return amount of hits made
     */
    int getHitCount();

    /**
     * Gets amount of sunk ships
     * @return amount of sunk ships
     */
    int getShipsSunk();

    /**
     * Gets amount of damaged ships
     * @return amount of damaged ships
     */
    int getDamagedShipsCount();

    /**
     * Gets amount of undiscovered ships
     * @return amount of undiscovered ships
     */
    int getUntouchedShipsCount();

    /**
     * Checks if game is over(all ships were sunk0
     * @return true if game has ended, false otherwise
     */
    boolean isGameOver();

    /**
     * Returns states of ocean cells
     * @return 2-d array of ocean cells states
     */
    CellState[][] getPlayerOceanState();

    CellState[][] getOpponentOceanState();

    void setClientServer(IClientServer clientServer);

    IClientServer getClientServer();

    SimpleBooleanProperty hasOpponent();

    void setOpponentName(String name);

    String getOpponentName();

    void setPlayerName(String name);

    String getPlayerName();

    String getWinnerName();

    String getLoserName();

    int getPlayerShotsCount();

    int getEnemyShotsCount();

    public static enum CellState {
        EMPTY, MISS, DAMAGED, SUNK
    }

    public static enum GameTurn {
        ME, OPPONENT, CALCULATING, END
    }
}
