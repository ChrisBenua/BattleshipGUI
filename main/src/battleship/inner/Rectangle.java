package battleship.inner;

public class Rectangle {
    private Point leftTopPoint;
    private Point rightBottomPoint;

    public Rectangle(Point leftTopPoint, Point rightBottomPoint) {
        if (leftTopPoint.x > rightBottomPoint.x || leftTopPoint.y > rightBottomPoint.y) {
            throw new IllegalArgumentException("Wrong points positioning");
        }
        this.leftTopPoint = leftTopPoint;
        this.rightBottomPoint = rightBottomPoint;
    }

    public Rectangle(Point leftTopPoint, int length, int height) {
        if (length < 0 || height < 0) {
            throw new IllegalArgumentException("Length or Height can not be negative");
        }
        this.leftTopPoint = leftTopPoint;
        this.rightBottomPoint = new Point(leftTopPoint.x + length, leftTopPoint.y + height);
    }

    public boolean intersectsWith(Rectangle rectangle) {
        int maxX = Math.max(this.leftTopPoint.x, rectangle.leftTopPoint.x);
        int minX = Math.min(this.rightBottomPoint.x, rectangle.rightBottomPoint.x);
        boolean Xintersection = maxX <= minX;
        int maxY = Math.max(this.leftTopPoint.y, rectangle.leftTopPoint.y);
        int minY = Math.min(this.rightBottomPoint.y, rectangle.rightBottomPoint.y);

        return Xintersection && (maxY <= minY);
    }

    public boolean isIn(Point point) {
        boolean inX = point.x <= rightBottomPoint.x && point.x >= leftTopPoint.x;
        boolean inY = point.y >= leftTopPoint.y && point.y <= rightBottomPoint.y;

        return inX && inY;
    }

    public Point getLeftTopPoint() {
        return leftTopPoint;
    }

    public Point getRightBottomPoint() {
        return rightBottomPoint;
    }

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
