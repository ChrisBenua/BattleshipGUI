package battleship.inner;

public interface IHitAdapterCollection {
    void add(IHitAdapter adapter);

    boolean isPointInsideAnyShip(Rectangle.Point point);

    boolean hit(Rectangle.Point target);

    ShipPartState shipPartState(Rectangle.Point point);

    int getSunkCount();
}
