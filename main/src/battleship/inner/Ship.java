package battleship.inner;

/**
 * Class for storing ship data
 */
public class Ship {
    private int bowRow;
    private int bowColumn;

    private boolean isHorizontal;
    private int length;

    private boolean[] hit;

    private String shipType;

    /**
     * Constructs new Ship with given length and ship type
     * @param length length of ship
     * @param shipType type of ship
     */
    public Ship(int length, String shipType) {
        this.length = length;
        this.shipType = shipType;

        hit = new boolean[length];
    }

    /**
     * Gets hit array of ship
     * @return hit array
     */
    public boolean[] getHit() {
        return hit;
    }

    /**
     * Check if this ship is sunk
     * @return true if ship is sunk, false otherwise
     */
    public boolean isSunk() {
        boolean isSunk = true;
        for (int i = 0; i < hit.length; ++i) {
            isSunk &= hit[i];
        }

        return isSunk;
    }

    /**
     * Sets bow row of this ship
     * @param bowRow y-coord of ship's bow on 2d-plane
     */
    public void setBowRow(int bowRow) {
        this.bowRow = bowRow;
    }

    /**
     * Sets bow column of this ship
     * @param bowColumn x-coord of ship's bow on 2d-plane
     */
    public void setBowColumn(int bowColumn) {
        this.bowColumn = bowColumn;
    }

    /**
     * Sets flag of horizontal placement
     * @param horizontal should ship be placed horizontally
     */
    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    /**
     * Gets ship type
     * @return ship type
     */
    public String getShipType() {
        return shipType;
    }

    /**
     * Gets this ship bow row
     * @return bow row
     */
    public int getBowRow() {
        return bowRow;
    }

    /**
     * Gets this ship bow column
     * @return bow column
     */
    public int getBowColumn() {
        return bowColumn;
    }

    /**
     * Gets flag of horizontal placement
     * @return true if ship is placed horizontally, false otherwise
     */
    public boolean isHorizontal() {
        return isHorizontal;
    }

    /**
     * Gets length of ship
     * @return length
     */
    public int getLength() {
        return length;
    }
}
