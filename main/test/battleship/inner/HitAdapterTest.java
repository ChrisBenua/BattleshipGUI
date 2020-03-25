package battleship.inner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HitAdapterTest {

    @Test
    void isPointInsideShip() {
        Ship ship = new ShipFactory().submarine();

        ship.setBowColumn(1);
        ship.setBowRow(1);
        ship.setHorizontal(true);

        var hitAdapter = new HitAdapter(ship);

        assertTrue(hitAdapter.isPointInsideShip(Rectangle.Point.of(1, 1)));
        assertFalse(hitAdapter.isPointInsideShip(Rectangle.Point.of(2, 2)));

        ship = new ShipFactory().destroyer();
        ship.setHorizontal(true);
        ship.setBowRow(1);
        ship.setBowColumn(1);

        hitAdapter = new HitAdapter(ship);

        assertTrue(hitAdapter.isPointInsideShip(Rectangle.Point.of(1, 1)));
        assertTrue(hitAdapter.isPointInsideShip(Rectangle.Point.of(2, 1)));
        assertFalse(hitAdapter.isPointInsideShip(Rectangle.Point.of(3, 1)));
        assertFalse(hitAdapter.isPointInsideShip(Rectangle.Point.of(2, 2)));

        ship = new ShipFactory().destroyer();
        ship.setHorizontal(false);
        ship.setBowRow(1);
        ship.setBowColumn(1);

        hitAdapter = new HitAdapter(ship);
        assertTrue(hitAdapter.isPointInsideShip(Rectangle.Point.of(1, 1)));
        assertTrue(hitAdapter.isPointInsideShip(Rectangle.Point.of(1, 2)));
        assertFalse(hitAdapter.isPointInsideShip(Rectangle.Point.of(1, 3)));
        assertFalse(hitAdapter.isPointInsideShip(Rectangle.Point.of(2, 2)));
    }

    @Test
    void hit() {
        var ship = new ShipFactory().battleShip();
        ship.setHorizontal(true);
        ship.setBowRow(1);
        ship.setBowColumn(1);

        var hitAdapter = new HitAdapter(ship);

        assertTrue(hitAdapter.hit(Rectangle.Point.of(1, 1)));
        assertTrue(ship.getHit()[0]);
        assertFalse(hitAdapter.hit(Rectangle.Point.of(1, 1)));

        assertTrue(hitAdapter.hit(Rectangle.Point.of(2, 1)));
        assertTrue(ship.getHit()[1]);
        assertFalse(hitAdapter.hit(Rectangle.Point.of(2, 1)));

        assertTrue(hitAdapter.hit(Rectangle.Point.of(3, 1)));
        assertTrue(ship.getHit()[2]);
        assertFalse(hitAdapter.hit(Rectangle.Point.of(3, 1)));

        assertTrue(hitAdapter.hit(Rectangle.Point.of(4, 1)));
        assertTrue(ship.getHit()[3]);
        assertFalse(hitAdapter.hit(Rectangle.Point.of(4, 1)));
    }

    @Test
    void shipPartState() {
        var ship = new ShipFactory().destroyer();
        ship.setHorizontal(true);
        ship.setBowRow(1);
        ship.setBowColumn(1);

        var hitAdapter = new HitAdapter(ship);

        assertEquals(hitAdapter.shipPartState(Rectangle.Point.of(1, 1)), ShipPartState.ALIVE);
        assertEquals(hitAdapter.shipPartState(Rectangle.Point.of(2, 1)), ShipPartState.ALIVE);
        hitAdapter.hit(Rectangle.Point.of(1, 1));
        assertEquals(hitAdapter.shipPartState(Rectangle.Point.of(1, 1)), ShipPartState.DAMAGED);
        hitAdapter.hit(Rectangle.Point.of(2, 1));
        assertEquals(hitAdapter.shipPartState(Rectangle.Point.of(2, 1)), ShipPartState.SUNK);
        assertEquals(hitAdapter.shipPartState(Rectangle.Point.of(1, 1)), ShipPartState.SUNK);
    }
}