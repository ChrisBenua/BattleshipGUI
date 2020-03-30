package battleship.guiservices;

import battleship.inner.IBattleshipGame;
import javafx.scene.paint.Color;

/**
 * Mapper from Ocean Cell State to its fill color
 */
public class OceanCellStateColorMapper implements IOceanCellStateColorMapper {
    @Override
    public Color map(IBattleshipGame.CellState state) {
        if (state == IBattleshipGame.CellState.SUNK) {
            return Color.BLACK;
        } else if (state == IBattleshipGame.CellState.DAMAGED) {
            return Color.RED;
        } else if (state == IBattleshipGame.CellState.EMPTY) {
            return Color.WHITE;
        } else if (state == IBattleshipGame.CellState.MISS) {
            return Color.LIGHTGRAY;
        }
        return Color.TRANSPARENT;
    }
}
