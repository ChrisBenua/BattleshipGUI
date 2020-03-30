package battleship.gui;

import battleship.guiservices.IOceanCellEventHandlerFactory;
import battleship.guiservices.IOceanCellStateColorMapper;
import battleship.inner.BattleshipGame;
import battleship.inner.IBattleshipGame;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class OceanGridPane extends GridPane {
    IBattleshipGame.CellState[][] states;
    IOceanCellStateColorMapper mapper;
    IOceanCellEventHandlerFactory factory;
    IRefreshable<RootPane.RefreshValues> parent;

    public OceanGridPane(double width, double height, IBattleshipGame.CellState[][] states,
                         IOceanCellStateColorMapper mapper, IOceanCellEventHandlerFactory factory,
                         IRefreshable<RootPane.RefreshValues> parent) {
        super.setWidth(width);
        super.setHeight(height);

        this.states = states;
        this.mapper = mapper;
        this.factory = factory;
        this.parent = parent;

        this.widthProperty().addListener((___, ____, __) -> {
            this.paint();
        });

        this.heightProperty().addListener((___, ____, __) -> {
            this.paint();
        });

        //paint();
    }

    public void setStates(IBattleshipGame.CellState[][] states) {
        this.states = states;
        paint();
    }

    private void paint() {
        this.getChildren().clear();
        double minSz = Math.min(getWidth(), getHeight());
        System.out.println(minSz);
        for (int row = 0; row < BattleshipGame.OCEAN_SIZE; ++row) {
            for (int col = 0; col < BattleshipGame.OCEAN_SIZE; ++col) {
                var cell = new OceanCell(
                        minSz / (BattleshipGame.OCEAN_SIZE + 1) - 2,
                        minSz / (BattleshipGame.OCEAN_SIZE + 1) - 2,
                        mapper.map(states[row][col])
                );
                cell.setOnMouseClicked(factory.getOceanCellEventHandler(row, col, parent));
                this.add(cell, col + 1, row + 1);
            }
        }

        for (int row = 0; row < BattleshipGame.OCEAN_SIZE; ++row) {
            var label = new Label(String.valueOf(row));
            this.add(label, 0, row + 1);
        }

        for (int column = 0; column < BattleshipGame.OCEAN_SIZE; ++column) {
            var label = new Label(String.valueOf(column));

            this.add(label, column + 1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }
}
