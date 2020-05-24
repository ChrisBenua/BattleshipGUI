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
        Rectangle finalNewShipRect = RectangleUtil.rectForShip(ship);

        if (finalNewShipRect.getRightBottomPoint().getX() > width || finalNewShipRect.getRightBottomPoint().getY() > height) {
            return false;
        }

        if (busyRectangles.stream().anyMatch(rect -> rect.intersectsWith(finalNewShipRect))) {
            return false;
        } else {
            if (shouldAdd) {
                busyRectangles.add(finalNewShipRect);
            }
            return true;
        }
    }

    @Override
    public void clear() {
        busyRectangles.clear();
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
