package battleship.inner;

public interface IBattleshipGame {
    void placeShipsRandomly();

    boolean shootAt(int row, int column);

    int getShotsFired();

    int getHitCount();

    int getShipsSunk();

    boolean isGameOver();

    OceanState[][] getOceanState();

    public static enum OceanState {
        EMPTY, MISS, DAMAGED, SUNK
    }
}
