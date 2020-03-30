package battleship.inner;

import battleship.gui.RootPane;

public interface IBattleshipGame {
    void placeShipsRandomly();

    BattleshipGame.ShotResults shootAt(int row, int column);

    void setSubscriber(ISubscriber<RootPane.GameEvents> subscriber);

    int getShotsFired();

    int getHitCount();

    int getShipsSunk();

    int getDamagedShipsCount();

    int getUntouchedShipsCount();

    boolean isGameOver();

    CellState[][] getOceanState();

    public static enum CellState {
        EMPTY, MISS, DAMAGED, SUNK
    }
}
