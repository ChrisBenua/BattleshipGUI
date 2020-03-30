package battleship.inner;

/**
 * Defines interface for creating different types of ships
 */
public interface IShipFactory {
    /**
     * Creates ship with length 1 and type "Submarine"
     * @return new "Submarine" ship
     */
    public Ship submarine();

    /**
     * Creates ship with length 2 and type "Destroyer"
     * @return new "Submarine" ship
     */
    public Ship destroyer();

    /**
     * Creates ship with length 3 and type "Cruiser"
     * @return new "Cruiser" ship
     */
    public Ship cruiser();

    /**
     * Create ship with length 4 and type "Battleship"
     * @return new "Battleship" ship
     */
    public Ship battleShip();
}
