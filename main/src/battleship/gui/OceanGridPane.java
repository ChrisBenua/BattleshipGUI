package battleship.gui;

import battleship.inner.BattleshipGame;
import javafx.scene.layout.GridPane;

public class OceanGridPane extends GridPane {
    public OceanGridPane(double width, double height) {
        for (int row = 0; row < BattleshipGame.OCEAN_SIZE; ++row) {
            for (int col = 0; col < BattleshipGame.OCEAN_SIZE; ++col) {
                this.add(new OceanCell(width / BattleshipGame.OCEAN_SIZE, height / BattleshipGame.OCEAN_SIZE),
                        col, row);
            }
        }
    }
}
