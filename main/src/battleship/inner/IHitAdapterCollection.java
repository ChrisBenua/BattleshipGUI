package battleship.inner;

public interface IHitAdapterCollection {
    void add(IHitAdapter adapter);

    boolean isPointInsideAnyShip(Rectangle.Point point);

    HitResults hit(Rectangle.Point target);

    ShipPartState shipPartState(Rectangle.Point point);

    int getSunkCount();

    int getDamagedCount();

    void setLogger(IEventsLogger logger);

    public static enum HitResults {
        MISS, HIT, SUNK
    }
}
