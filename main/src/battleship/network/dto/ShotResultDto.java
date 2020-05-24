package battleship.network.dto;

import java.io.Serializable;

/**
 * DTO for sending shot result to opponent
 */
public class ShotResultDto implements Serializable, ITypedDto {
    public static final String TYPE = "ShotResult";

    /**
     * Row of shot
     */
    private int row;
    /**
     * Column of shot
     */
    private int col;
    /**
     * Shot result
     */
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
