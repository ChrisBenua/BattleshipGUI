package battleship.inner;

public class RectangleUtil {
    public static Rectangle rectForShip(Ship ship) {
        if (ship.isHorizontal()) {
            return new Rectangle(Rectangle.Point.of(ship.getBowColumn(), ship.getBowRow()), ship.getLength(), 1);
        } else {
            return new Rectangle(Rectangle.Point.of(ship.getBowColumn(), ship.getBowRow()), 1, ship.getLength());
        }
    }
}
