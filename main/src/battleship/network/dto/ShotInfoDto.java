package battleship.network.dto;

import java.io.Serializable;

/**
 * DTO for sending info about shots
 */
public class ShotInfoDto implements Serializable, ITypedDto {
    public static final String TYPE = "ShotInfo";

    private String type = TYPE;
    /**
     * Row of shot
     */
    private int row;
    /**
     * Column of shot
     */
    private int col;

    public ShotInfoDto() {
        row = col = 0;
    }

    public ShotInfoDto(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setType(String type) {
        this.type = TYPE;
    }

    @Override
    public String getType() {
        return type;
    }
}
