package battleship.inner;

public interface IHitAdapter {

    Ship getShip();

    boolean isPointInsideShip(Rectangle.Point point);

    boolean hit(Rectangle.Point target);

    ShipPartState shipPartState(Rectangle.Point point);
}
