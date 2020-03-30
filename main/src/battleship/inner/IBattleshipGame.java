package battleship.inner;

import battleship.gui.RootPane;

/**
 * Interfaces for defining main operations with game
 */
public interface IBattleshipGame {
    /**
     * Places ships randomly, validating their position
     */
    void placeShipsRandomly();

    /**
     * Performs a shot at given position
     * @param row targeted row
     * @param column targeted column
     * @return MISS if shot didnt deal any damage, HIT, it ship was damaged, SUNK if ship got sunk after this shot
     */
    BattleshipGame.ShotResults shootAt(int row, int column);

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
    CellState[][] getOceanState();

    public static enum CellState {
        EMPTY, MISS, DAMAGED, SUNK
    }
}
