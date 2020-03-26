package battleship.inner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HitAdapterCollectionTest {

    private HitAdapterCollection prepareData1() {
        var collection = new HitAdapterCollection();
        var adapterFactory = new HitAdapterFactory();
        var shipFactory = new ShipFactory();

        var ship1 = shipFactory.battleShip();
        ship1.setHorizontal(true);
        ship1.setBowRow(4);
        ship1.setBowColumn(5);

        collection.add(adapterFactory.hitAdapterFor(ship1));

        var ship2 = shipFactory.cruiser();
        ship2.setHorizontal(false);
        ship2.setBowRow(3);
        ship2.setBowColumn(3);

        collection.add(adapterFactory.hitAdapterFor(ship2));

        var ship3 = shipFactory.cruiser();
        ship3.setHorizontal(false);
        ship3.setBowRow(6);
        ship3.setBowColumn(1);

        collection.add(adapterFactory.hitAdapterFor(ship3));

        var ship4 = shipFactory.destroyer();
        var ship5 = shipFactory.destroyer();
        var ship6 = shipFactory.destroyer();

        ship4.setHorizontal(false);
        ship5.setHorizontal(false);
        ship6.setHorizontal(false);

        ship4.setBowRow(0);
        ship4.setBowColumn(1);

        ship5.setBowRow(8);
        ship5.setBowColumn(6);

        ship6.setBowColumn(4);
        ship6.setBowRow(8);

        var ship7 = shipFactory.submarine();
        var ship8 = shipFactory.submarine();
        var ship9 = shipFactory.submarine();
        var ship10 = shipFactory.submarine();

        ship7.setBowRow(0);
        ship7.setBowColumn(7);

        ship8.setBowRow(2);
        ship8.setBowColumn(5);

        ship9.setBowRow(6);
        ship9.setBowColumn(6);

        ship10.setBowRow(4);
        ship10.setBowColumn(1);

        collection.add(adapterFactory.hitAdapterFor(ship4));
        collection.add(adapterFactory.hitAdapterFor(ship5));
        collection.add(adapterFactory.hitAdapterFor(ship6));
        collection.add(adapterFactory.hitAdapterFor(ship7));
        collection.add(adapterFactory.hitAdapterFor(ship8));
        collection.add(adapterFactory.hitAdapterFor(ship9));
        collection.add(adapterFactory.hitAdapterFor(ship10));

        return collection;
    }

    @Test
    void isPointInsideAnyShip() {
        var collection = prepareData1();

        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(1, 0)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(1, 1)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(7, 0)));
        assertFalse(collection.isPointInsideAnyShip(Rectangle.Point.of(0 ,0)));

        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(1, 4)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(1, 6)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(1, 8)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(4, 8)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(4, 9)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(6, 8)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(6, 9)));
        assertTrue(collection.isPointInsideAnyShip(Rectangle.Point.of(6, 6)));
    }

    @Test
    void hit() {
        var collection = prepareData1();
        assertTrue(collection.hit(Rectangle.Point.of(7, 0)));
        assertEquals(collection.getSunkCount(), 1);
        assertEquals(collection.shipPartState(Rectangle.Point.of(7, 0)), ShipPartState.SUNK);

        assertFalse(collection.hit(Rectangle.Point.of(7, 0)));
        assertEquals(collection.getSunkCount(), 1);

        assertFalse(collection.hit(Rectangle.Point.of(0, 0)));
        assertTrue(collection.hit(Rectangle.Point.of(1, 0)));
        assertEquals(collection.shipPartState(Rectangle.Point.of(1, 0)), ShipPartState.DAMAGED);
        assertTrue(collection.hit(Rectangle.Point.of(1, 1)));
        assertEquals(collection.shipPartState(Rectangle.Point.of(1, 0)), ShipPartState.SUNK);
        assertEquals(collection.shipPartState(Rectangle.Point.of(1, 1)), ShipPartState.SUNK);

        assertEquals(collection.getSunkCount(), 2);
    }
}