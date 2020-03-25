package battleship.inner;

public interface IPlacementAdapter {
    boolean canAddShip(Ship ship, boolean shouldAdd);

    void setWidth(int width);

    void setHeight(int height);
}
