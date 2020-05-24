package battleship.inner;

import java.util.List;
import java.util.function.Supplier;

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

    @Override
    public Ship shipByLength(int len) {
        List<Supplier<Ship>> lst = List.of(this::submarine, this::destroyer, this::cruiser, this::battleShip);
        return lst.get(len - 1).get();
    }
}
