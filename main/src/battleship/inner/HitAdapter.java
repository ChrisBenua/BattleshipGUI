package battleship.inner;

public class HitAdapter implements IHitAdapter {
    private Ship ship;
    private Rectangle rectangle;

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

    private int getPosition(Rectangle.Point point) {
        if (ship.isHorizontal()) {
            return point.getX() - rectangle.getLeftTopPoint().getX();
        } else {
            return point.getY() - rectangle.getLeftTopPoint().getY();
        }
    }

    @Override
    public Ship getShip() {
        return ship;
    }

    @Override
    public boolean isPointInsideShip(Rectangle.Point point) {
        return this.rectangle.isIn(point);
    }

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

    @Override
    public ShipPartState shipPartState(Rectangle.Point point) {
        if (!this.rectangle.isIn(point)) {
            throw new IllegalArgumentException("target point is outside of ship");
        }

        boolean[] hit = ship.getHit();
        boolean isSunk = true;

        for (int i = 0; i < ship.getLength(); ++i) {
            isSunk &= hit[i];
        }

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
