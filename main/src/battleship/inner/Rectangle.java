package battleship.inner;

/**
 * Class responsible for handling rectangles intersection and storing
 */
public class Rectangle {
    private Point leftTopPoint;
    private Point rightBottomPoint;

    /**
     * Constructs new rectangle with given leftmost top point and rightmost bottom point
     * @param leftTopPoint leftmost top point
     * @param rightBottomPoint rightmost bottom point
     */
    public Rectangle(Point leftTopPoint, Point rightBottomPoint) {
        if (leftTopPoint.x > rightBottomPoint.x || leftTopPoint.y > rightBottomPoint.y) {
            throw new IllegalArgumentException("Wrong points positioning");
        }
        this.leftTopPoint = leftTopPoint;
        this.rightBottomPoint = rightBottomPoint;
    }

    /**
     * Constructs new rectangle with given leftmost top point, length and height
     * @param leftTopPoint leftmost top point
     * @param length length of rectangle
     * @param height height of rectangle
     */
    public Rectangle(Point leftTopPoint, int length, int height) {
        if (length < 0 || height < 0) {
            throw new IllegalArgumentException("Length or Height can not be negative");
        }
        this.leftTopPoint = leftTopPoint;
        this.rightBottomPoint = new Point(leftTopPoint.x + length, leftTopPoint.y + height);
    }

    /**
     * Checks intersection with given rectangle
     * @param rectangle rectangle to check intersection with
     * @return true if there is intersection, false otherwise
     */
    public boolean intersectsWith(Rectangle rectangle) {
        int maxX = Math.max(this.leftTopPoint.x, rectangle.leftTopPoint.x);
        int minX = Math.min(this.rightBottomPoint.x, rectangle.rightBottomPoint.x);
        boolean Xintersection = maxX <= minX;
        int maxY = Math.max(this.leftTopPoint.y, rectangle.leftTopPoint.y);
        int minY = Math.min(this.rightBottomPoint.y, rectangle.rightBottomPoint.y);

        return Xintersection && (maxY <= minY);
    }

    /**
     * Checks if point is inside current rectangle
     * @param point given point
     * @return true if point is inside this rectangle, false otherwise
     */
    public boolean isIn(Point point) {
        boolean inX = point.x <= rightBottomPoint.x && point.x >= leftTopPoint.x;
        boolean inY = point.y >= leftTopPoint.y && point.y <= rightBottomPoint.y;

        return inX && inY;
    }

    /**
     * Gets leftmost top point of this rectangle
     * @return leftmost top point
     */
    public Point getLeftTopPoint() {
        return leftTopPoint;
    }

    /**
     * Gets rightmost bottom point of this rectangle
     * @return rightmost bottom point
     */
    public Point getRightBottomPoint() {
        return rightBottomPoint;
    }

    /**
     * class for storing Point on 2d plane
     */
    public static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public static Point of(int x, int y) {
            return new Point(x, y);
        }
    }
}
