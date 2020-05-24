package battleship.network.dto;

import java.io.Serializable;


public class ShotResultDto implements Serializable, ITypedDto {
    public static final String TYPE = "ShotResult";

    private int row;
    private int col;
    private ShotResultEnum shotResult;
    private String type = TYPE;

    public ShotResultDto() {
        row = col = 0;
        shotResult = ShotResultEnum.MISS;
    }

    public ShotResultDto(int row, int col, ShotResultEnum shotResult) {
        this.row = row;
        this.col = col;
        this.shotResult = shotResult;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public ShotResultEnum getShotResult() {
        return shotResult;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
