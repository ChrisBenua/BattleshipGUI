package battleship.guiservices;

import battleship.inner.IBattleshipGame;
import javafx.scene.paint.Color;

/**
 * Mapper from Ocean State to its Color
 */
public interface IOceanCellStateColorMapper {
    public Color map(IBattleshipGame.CellState state);
}
