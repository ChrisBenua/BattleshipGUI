package battleship.inner;

import battleship.gui.IRefreshable;
import battleship.gui.RootPane;
import battleship.network.IClientServer;
import battleship.network.IDtoEventsHandler;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

/**
 * Interfaces for defining main operations with game
 */
public interface IBattleshipGame extends IDtoEventsHandler {
    /**
     * Places ships randomly, validating their position
     */
    void placeShipsRandomly();

    /**
     * Adds listener for refreshing
     * @param onUpdateOpponentField event handler
     */
    void addOnUpdateField(IRefreshable<RootPane.RefreshValues> onUpdateOpponentField);

    /**
     * Shoots at opponent field
     * @param row row
     * @param column column
     */
    void shootAtOpponentField(int row, int column);

    /**
     * Sets opponent ready status
     * @param status is opponent ready
     */
    void setOpponentReadyStatus(boolean status);

    /**
     * Gets property indicating whether opponent is ready
     * @return property
     */
    ObservableBooleanValue getOpponentReadyStatus();

    /**
     * Gets player ready status
     * @param status is player ready
     */
    void setPlayerReadyStatus(boolean status);

    /**
     * Gets property indicating whether player is ready
     * @return property
     */
    ObservableBooleanValue getPlayerReadyStatus();

    /**
     * Checks if player's turn is now
     * @return true if player should shoot now false otherwise
     */
    boolean isMyTurn();

    /**
     * Gets property indicating that player's turn is now
     * @return property indicating that player's turn is now
     */
    ReadOnlyBooleanProperty getIsMyTurn();

    /**
     * Gets property indicating current game state
     * @return property indicating current game state
     */
    ReadOnlyObjectProperty<GameTurn> getGameTurn();

    /**
     * Sets current game turn
     * @param turn game state
     */
    void setTurn(GameTurn turn);

    /**
     * Sets turn
     * @param turn true if player should shoot false otherwise
     */
    void setMyTurn(boolean turn);

    /**
     * Sets game events subscriber
     * @param subscriber event handler
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

    /**
     * Returns states of opponent ocean
     * @return 2-d array of ocean cells states
     */
    CellState[][] getOpponentOceanState();

    /**
     * Sets IClientServer
     * @param clientServer instance for handling network connections
     */
    void setClientServer(IClientServer clientServer);

    /**
     * Gets client server
     * @return IClientServer impl
     */
    IClientServer getClientServer();

    /**
     * Gets property indicating that player has found opponent
     * @return property indicating that player has found opponent
     */
    SimpleBooleanProperty hasOpponent();

    /**
     * Sets opponent name
     * @param name opponent's name
     */
    void setOpponentName(String name);

    /**
     * Gets opponent name
     * @return opponent's name
     */
    String getOpponentName();

    /**
     * Sets player name
     * @param name player name
     */
    void setPlayerName(String name);

    /**
     * Gets player name
     * @return player name
     */
    String getPlayerName();

    /**
     * Gets winner name
     * @return winner name
     */
    String getWinnerName();

    /**
     * Gets loser name
     * @return loser name
     */
    String getLoserName();

    /**
     * Gets amount of shots done by player
     * @return amount of shots done by player
     */
    int getPlayerShotsCount();

    /**
     * Gets shots
     * @return
     */
    int getEnemyShotsCount();

    public static enum CellState {
        EMPTY, MISS, DAMAGED, SUNK
    }

    public static enum GameTurn {
        ME, OPPONENT, CALCULATING, END
    }
}
