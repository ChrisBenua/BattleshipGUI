package battleship.gui;

import battleship.guiservices.IOceanCellEventHandlerFactory;
import battleship.guiservices.IOceanCellStateColorMapper;
import battleship.inner.BattleshipGame;
import battleship.inner.IBattleshipGame;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * GridPane for interacting with player's/opponent's field
 */
public class OceanGridPane extends GridPane {
    IBattleshipGame.CellState[][] states;
    IOceanCellStateColorMapper mapper;
    IOceanCellEventHandlerFactory factory;
    IRefreshable<RootPane.RefreshValues> parent;
    OceanCell[][] cells;
    ObservableBooleanValue binding;

    /**
     * Creates new OceanGridPane
     * @param states current state of each cell
     * @param mapper maps state of cell to its color
     * @param factory factory for generating click handlers
     * @param parent parent to receive notifications
     * @param binding binding for availability of clicks on cells
     */
    public OceanGridPane(IBattleshipGame.CellState[][] states,
                         IOceanCellStateColorMapper mapper, IOceanCellEventHandlerFactory factory,
                         IRefreshable<RootPane.RefreshValues> parent, ObservableBooleanValue binding) {

        cells = new OceanCell[BattleshipGame.OCEAN_SIZE][BattleshipGame.OCEAN_SIZE];
        this.binding = binding;
        this.states = states;
        this.mapper = mapper;
        this.factory = factory;
        this.parent = parent;

        this.binding.addListener((obs, old, new_) -> {
            if (new_) {
                enableMouseClicks();
            } else {
                disableMouseClicks();
            }
        });

        paint();

        if (this.binding.get()) {
            enableMouseClicks();
        }
    }

    /**
     * adds click handler for each cell
     */
    private void enableMouseClicks() {
        for (int i = 0; i < BattleshipGame.OCEAN_SIZE; ++i) {
            for (int j = 0; j < BattleshipGame.OCEAN_SIZE; ++j) {
                cells[i][j].setOnMouseClicked(factory.getOceanCellEventHandler(i, j, parent));
            }
        }
    }

    /**
     * Removes click handlers for each cell
     */
    private void disableMouseClicks() {
        for (int i = 0; i < BattleshipGame.OCEAN_SIZE; ++i) {
            for (int j = 0; j < BattleshipGame.OCEAN_SIZE; ++j) {
                cells[i][j].setOnMouseClicked(null);
            }
        }
    }

    /**
     * Updates cells colors
     * @param states new states of cells
     */
    public void setStates(IBattleshipGame.CellState[][] states) {
        this.states = states;
        for (int row = 0; row < BattleshipGame.OCEAN_SIZE; ++row) {
            for (int col = 0; col < BattleshipGame.OCEAN_SIZE; ++col) {
                cells[row][col].updateFill(mapper.map(states[row][col]));
            }
        }
    }

    /**
     * Layout
     */
    private void paint() {
        this.getChildren().clear();
        double minSz = Math.min(getWidth(), getHeight());
        ArrayList<Node> nodes = new ArrayList<>();
        var binding = Bindings.min(widthProperty(), heightProperty());
        System.out.println(minSz);
        for (int row = 0; row < BattleshipGame.OCEAN_SIZE; ++row) {
            for (int col = 0; col < BattleshipGame.OCEAN_SIZE; ++col) {
                var cell = new OceanCell(
                        mapper.map(states[row][col]),
                        binding.divide(BattleshipGame.OCEAN_SIZE + 1)
                );
                //cell.setOnMouseClicked(factory.getOceanCellEventHandler(row, col, parent));
                GridPane.setColumnIndex(cell,col + 1);
                GridPane.setRowIndex(cell, row + 1);
                cells[row][col] = cell;
                nodes.add(cell);
            }
        }

        for (int row = 0; row < BattleshipGame.OCEAN_SIZE; ++row) {
            var label = new Label(String.valueOf(row));
            GridPane.setRowIndex(label, row + 1);
            GridPane.setColumnIndex(label, 0);
            nodes.add(label);
        }

        for (int column = 0; column < BattleshipGame.OCEAN_SIZE; ++column) {
            var label = new Label(String.valueOf(column));

            GridPane.setRowIndex(label, 0);
            GridPane.setColumnIndex(label, column + 1);
            GridPane.setHalignment(label, HPos.CENTER);
            nodes.add(label);
        }

        this.getChildren().addAll(nodes);
        layoutChildren();
    }
}
