package battleship.inner;

public class Ship {
    private int bowRow;
    private int bowColumn;

    private boolean isHorizontal;
    private int length;

    private boolean[] hit;

    private String shipType;

    public Ship(int length, String shipType) {
        this.length = length;
        this.shipType = shipType;

        hit = new boolean[length];
    }

    public boolean[] getHit() {
        return hit;
    }

    public void setBowRow(int bowRow) {
        this.bowRow = bowRow;
    }

    public void setBowColumn(int bowColumn) {
        this.bowColumn = bowColumn;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public String getShipType() {
        return shipType;
    }

    public int getBowRow() {
        return bowRow;
    }

    public int getBowColumn() {
        return bowColumn;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public int getLength() {
        return length;
    }
}
