package battleship.inner;

/**
 * Adapter/wrapper for Ship to perform shots and checking state
 */
public interface IHitAdapter {

    /**
     * Gets wrapped ship
     * @return ship
     */
    Ship getShip();

    /**
     * Check if point is inside wrapped ship
     * @param point target point to check
     * @return
     */
    boolean isPointInsideShip(Rectangle.Point point);

    /**
     * Performs hit to targeted point
     * @param target target point
     * @return true if ship was damaged, false otherwise
     */
    boolean hit(Rectangle.Point target);

    /**
     * get state of ship part at given point
     * @param point target point
     * @return ALIVE if part wasnt damaged, DAMAGED if it was damaged, SUNK if all parts are damaged
     */
    ShipPartState shipPartState(Rectangle.Point point);
}
