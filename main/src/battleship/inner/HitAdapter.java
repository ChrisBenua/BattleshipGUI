package battleship.inner;

/**
 * Handles hit logic for ships
 */
public class HitAdapter implements IHitAdapter {
    private Ship ship;
    private Rectangle rectangle;

    /**
     * Constructs new HitAdapter instance
     * @param ship ship to handle
     */
    public HitAdapter(Ship ship) {
        this.ship = ship;

        Rectangle.Point leftTopPoint = Rectangle.Point.of(ship.getBowColumn(), ship.getBowRow());

        Rectangle.Point rightBottomPoint = null;

        if (ship.isHorizontal()) {
            rightBottomPoint = Rectangle.Point.of(leftTopPoint.getX() + ship.getLength() - 1, leftTopPoint.getY());
        } else {
            rightBottomPoint = Rectangle.Point.of(leftTopPoint.getX(), leftTopPoint.getY() + ship.getLength() - 1);
        }
        this.rectangle = new Rectangle(leftTopPoint, rightBottomPoint);
    }

    /**
     * Gets position of point in hit array
     * @param point hit point
     * @return hit array index
     */
    private int getPosition(Rectangle.Point point) {
        if (ship.isHorizontal()) {
            return point.getX() - rectangle.getLeftTopPoint().getX();
        } else {
            return point.getY() - rectangle.getLeftTopPoint().getY();
        }
    }

    /**
     * Gets ship
     * @return ship
     */
    @Override
    public Ship getShip() {
        return ship;
    }

    /**
     * Checks if point is inside ship
     * @param point point to check
     * @return true if point is inside ship false otherwise
     */
    @Override
    public boolean isPointInsideShip(Rectangle.Point point) {
        return this.rectangle.isIn(point);
    }

    /**
     * Performs hit in given point
     * @param target target point
     * @return true if shot hits the target false otherwise
     */
    @Override
    public boolean hit(Rectangle.Point target) {
        if (!this.rectangle.isIn(target)) {
            throw new IllegalArgumentException("target point is outside of ship");
        }

        int position = getPosition(target);

        if (ship.getHit()[position]) {
            return false;
        } else {
            ship.getHit()[position] = true;
            return true;
        }
    }

    /**
     * Gets state of ship part at given point
     * @param point point of ship to get state of
     * @return ShipPartState
     */
    @Override
    public ShipPartState shipPartState(Rectangle.Point point) {
        if (!this.rectangle.isIn(point)) {
            throw new IllegalArgumentException("target point is outside of ship");
        }

        boolean[] hit = ship.getHit();
        boolean isSunk = ship.isSunk();

        if (isSunk) {
            return ShipPartState.SUNK;
        }

        int position = getPosition(point);

        if (hit[position]) {
            return ShipPartState.DAMAGED;
        } else {
            return ShipPartState.ALIVE;
        }
    }
}
