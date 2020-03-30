package battleship.inner;

import java.util.ArrayList;

/**
 * Class for handling valid ship placement
 */
public class PlacementAdapter implements IPlacementAdapter {
    private ArrayList<Rectangle> busyRectangles = new ArrayList<>();
    private int width;
    private int height;

    @Override
    public boolean canAddShip(Ship ship, boolean shouldAdd) {
        Rectangle newShipRect = null;

        if (ship.isHorizontal()) {
            newShipRect = new Rectangle(Rectangle.Point.of(ship.getBowColumn(), ship.getBowRow()), ship.getLength(), 1);
        } else {
            newShipRect = new Rectangle(Rectangle.Point.of(ship.getBowColumn(), ship.getBowRow()), 1, ship.getLength());
        }

        if (newShipRect.getRightBottomPoint().getX() > width || newShipRect.getRightBottomPoint().getY() > height) {
            return false;
        }

        Rectangle finalNewShipRect = newShipRect;
        if (busyRectangles.stream().anyMatch(rect -> rect.intersectsWith(finalNewShipRect))) {
            return false;
        } else {
            if (shouldAdd) {
                busyRectangles.add(newShipRect);
            }
            return true;
        }
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }
}
