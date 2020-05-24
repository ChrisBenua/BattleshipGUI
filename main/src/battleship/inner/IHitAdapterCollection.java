package battleship.inner;

/**
 * Interface for defining operations with collection of ships
 */
public interface IHitAdapterCollection {
    /**
     * Adds a ship to collectiom
     * @param adapter
     */
    void add(IHitAdapter adapter);

    /**
     * Checks is given point is inside any ship
     * @param point
     * @return true if there exists ship with this point inside, false otherwise
     */
    boolean isPointInsideAnyShip(Rectangle.Point point);

    /**
     * Performs shot at given point
     * @param target targeted point
     * @return MISS if there is no damage dealt, HIT if part of ship was damaged, SUNK of damaged ship got sunk
     */
    HitResults hit(Rectangle.Point target);

    /**
     * Gets state of ship's part placed at given point
     * @param point targeted point
     * @return ALIVE, DAMAGE or SUNK depends on cell state
     */
    ShipPartState shipPartState(Rectangle.Point point);

    /**
     * Gets amount of sunk ships
     * @return amount of sunk ships
     */
    int getSunkCount();

    /**
     * Gets amount of damaged ships
     * @return amount of damaged ships
     */
    int getDamagedCount();

    /**
     * Gets type of last sunk ship
     * @return type of last sunk ship
     */
    String getLastSunkShipType();

    /**
     * Clears ships
     */
    void clear();

    public static enum HitResults {
        MISS, HIT, SUNK
    }
}
