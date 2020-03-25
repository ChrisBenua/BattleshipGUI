package battleship.inner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RectangleTest {

    @Test
    void isIn() {
        Rectangle rectangle = new Rectangle(Rectangle.Point.of(1, 2), 3, 4);

        assertTrue(rectangle.isIn(Rectangle.Point.of(1, 2)));
        assertTrue(rectangle.isIn(Rectangle.Point.of(4, 2)));
        assertTrue(rectangle.isIn(Rectangle.Point.of(4, 6)));
    }

    @Test
    void intersection() {
        Rectangle rectangle = new Rectangle(Rectangle.Point.of(1, 2), 3, 4);

        Rectangle rectangle1 = new Rectangle(Rectangle.Point.of(3, 5), 1, 1);

        assertTrue(rectangle.intersectsWith(rectangle1));

        Rectangle rectangle2 = new Rectangle(Rectangle.Point.of(4, 6), 1, 1);

        assertTrue(rectangle.intersectsWith(rectangle2));

        Rectangle rectangle3 = new Rectangle(Rectangle.Point.of(2, 3), 1, 1);

        assertTrue(rectangle.intersectsWith(rectangle3));

        Rectangle rectangle4 = new Rectangle(Rectangle.Point.of(5, 7), 1, 1);

        assertFalse(rectangle.intersectsWith(rectangle4));

        Rectangle rectangle5 = new Rectangle(Rectangle.Point.of(1, 1), 1, 1);
        Rectangle rectangle6 = new Rectangle(Rectangle.Point.of(2, 2), 1, 1);
        assertTrue(rectangle5.intersectsWith(rectangle6));
    }
}