package battleship.inner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlacementAdapterTest {

    @Test
    void canAddShip() {
        PlacementAdapter adapter = new PlacementAdapter();
        adapter.setHeight(10);
        adapter.setWidth(10);

        var factory = new ShipFactory();
        var ship = factory.cruiser();
        ship.setHorizontal(false);
        ship.setBowRow(7);
        ship.setBowColumn(1);

        assertTrue(adapter.canAddShip(ship, false));
        ship.setBowRow(8);
        assertFalse(adapter.canAddShip(ship, false));
        ship.setBowRow(7);
        adapter.canAddShip(ship, true);

        var newShip = factory.submarine();
        newShip.setBowRow(7);
        newShip.setBowColumn(0);
        assertFalse(adapter.canAddShip(newShip, false));
        newShip.setBowRow(6);
        assertFalse(adapter.canAddShip(newShip, false));
        newShip.setBowRow(9);
        assertFalse(adapter.canAddShip(newShip, false));
        newShip.setBowRow(6);
        newShip.setBowColumn(1);
        assertFalse(adapter.canAddShip(newShip, false));
        newShip.setBowRow(5);
        assertTrue(adapter.canAddShip(newShip, false));
    }
}