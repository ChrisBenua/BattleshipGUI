package battleship.inner;

/**
 * Class responsible for creating ships
 */
public class ShipFactory implements IShipFactory {
    @Override
    public Ship submarine() {
        return new Ship(1, "Submarine");
    }

    @Override
    public Ship destroyer() {
        return new Ship(2, "Destroyer");
    }

    @Override
    public Ship cruiser() {
        return new Ship(3, "Cruiser");
    }

    @Override
    public Ship battleShip() {
        return new Ship(4, "BattleShip");
    }
}
