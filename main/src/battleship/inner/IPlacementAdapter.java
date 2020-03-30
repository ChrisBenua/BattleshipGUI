package battleship.inner;

/**
 * Defines interface for placement wrapper over ship collection
 */
public interface IPlacementAdapter {
    /**
     * Check if can add ship to collection of ships
     * @param ship new ship
     * @param shouldAdd should add on success
     * @return true if it is possible to add ship, false otherwise
     */
    boolean canAddShip(Ship ship, boolean shouldAdd);

    /**
     * sets width of ocean
     * @param width width in terms of cells
     */
    void setWidth(int width);

    /**
     * Sets height of ocean
     * @param height height in terms of cells
     */
    void setHeight(int height);
}
