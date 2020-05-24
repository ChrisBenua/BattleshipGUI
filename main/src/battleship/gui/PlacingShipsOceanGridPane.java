package battleship.gui;

import battleship.inner.*;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.JavaBeanBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class CurrentDragInfo {
    private int row;
    private int column;
    private int shipLen;
    private boolean isHorizontal;

    public CurrentDragInfo(int row, int column, int shipLen, boolean isHorizontal) {
        this.row = row;
        this.column = column;
        this.shipLen = shipLen;
        this.isHorizontal = isHorizontal;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getShipLen() {
        return shipLen;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }
}


public class PlacingShipsOceanGridPane extends GridPane implements IShipsContainerPaneEventHandler {
    private OceanCell[][] cells;
    private List<Ship> ships = new ArrayList<Ship>();
    private IPlacementAdapter adapter;
    private IHitAdapterCollection hitAdapterCollection;
    private CurrentDragInfo dragInfo = null;
    private IHitAdapterFactory factory;
    private SimpleBooleanProperty isFilledProperty = new SimpleBooleanProperty(false);
    private IShipFactory shipFactory;

    public PlacingShipsOceanGridPane(IShipFactory shipFactory,IPlacementAdapter adapter, IHitAdapterCollection hitAdapterCollection, IHitAdapterFactory hitAdapterFactory) {
        isFilledProperty.set(false);
        cells = new OceanCell[BattleshipGame.OCEAN_SIZE][BattleshipGame.OCEAN_SIZE];
        this.shipFactory = shipFactory;
        this.hitAdapterCollection = hitAdapterCollection;
        this.adapter = adapter;
        this.factory = hitAdapterFactory;

        paint();
    }

    public boolean isFilled() {
        return ships.size() == 10;
    }

    public List<Ship> getShips() {
        return ships;
    }

    private void updateCells() {
        for (int i = 0; i < BattleshipGame.OCEAN_SIZE; ++i) {
            for (int j = 0; j < BattleshipGame.OCEAN_SIZE; ++j) {
                if (hitAdapterCollection.isPointInsideAnyShip(new Rectangle.Point(j, i))) {
                    cells[i][j].updateFill(Color.GREENYELLOW);
                } else {
                    cells[i][j].updateFill(Color.WHITE);
                }
            }
        }

        if (Objects.nonNull(dragInfo)) {
            if (dragInfo.isHorizontal()) {
                for (int col = dragInfo.getColumn(); col < dragInfo.getColumn() + dragInfo.getShipLen(); ++col) {
                    cells[dragInfo.getRow()][col].updateFill(Color.LIGHTYELLOW);
                }
            } else {
                for (int row = dragInfo.getRow(); row < dragInfo.getRow() + dragInfo.getShipLen(); ++row) {
                    cells[row][dragInfo.getColumn()].updateFill(Color.LIGHTYELLOW);
                }
            }
        }
    }

    private void paint() {
        this.getChildren().clear();
        double minSz = Math.min(getWidth(), getHeight());
        ArrayList<Node> nodes = new ArrayList<>();
        var binding = Bindings.min(widthProperty(), heightProperty());
        binding.addListener((obs, old, new_) -> {
            System.out.println(new_);
        });
        System.out.println(minSz);
        for (int row = 0; row < BattleshipGame.OCEAN_SIZE; ++row) {
            for (int col = 0; col < BattleshipGame.OCEAN_SIZE; ++col) {
                var cell = new OceanCell(
                        Color.WHITE,
                        binding.divide(BattleshipGame.OCEAN_SIZE + 1)
                );
                int finalRow = row;
                int finalCol = col;
                cell.setOnDragOver((event) -> {
                    System.out.println(String.format("Drag over %d %d", finalRow, finalCol));
                    if (event.getDragboard().hasContent(ShipsPlacingPane.shipsDataFormat)) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }

                    event.consume();
                });

                cell.setOnDragEntered((event) -> {
                    var dto = (ShipDragBoardDto)event.getDragboard().getContent(ShipsPlacingPane.shipsDataFormat);

                    var ship = shipFactory.shipByLength(dto.shipLength);
                    ship.setBowColumn(finalCol);
                    ship.setBowRow(finalRow);
                    ship.setHorizontal(dto.isHorizontal);

                    if (adapter.canAddShip(ship, false)) {
                        this.dragInfo = new CurrentDragInfo(finalRow, finalCol, dto.shipLength, ship.isHorizontal());
                    } else {
                        this.dragInfo = null;
                    }

                    event.consume();

                    this.updateCells();
                });

                cell.setOnDragDropped((event) -> {
                    var db = event.getDragboard();
                    boolean success = false;
                    if (db.hasContent(ShipsPlacingPane.shipsDataFormat)) {
                        var dto = (ShipDragBoardDto)event.getDragboard().getContent(ShipsPlacingPane.shipsDataFormat);

                        var ship = shipFactory.shipByLength(dto.shipLength);
                        ship.setBowColumn(finalCol);
                        ship.setBowRow(finalRow);
                        ship.setHorizontal(dto.isHorizontal);

                        if (adapter.canAddShip(ship, false)) {
                            ships.add(ship);

                            if (ships.size() == 10) {
                                isFilledProperty.set(true);
                            }

                            adapter.canAddShip(ship, true);
                            hitAdapterCollection.add(factory.hitAdapterFor(ship));
                            success = true;
                        } else {
                            this.dragInfo = null;
                        }
                    }
                    /* let the source know whether the string was successfully
                     * transferred and used */
                    event.setDropCompleted(success);

                    updateCells();

                    event.consume();
                });

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

    @Override
    public void onDragDone() {
        dragInfo = null;
        this.updateCells();
    }

    @Override
    public void onClear() {
        this.hitAdapterCollection.clear();
        this.adapter.clear();
        this.updateCells();
        ships.clear();
        this.isFilledProperty.set(false);
    }

    public SimpleBooleanProperty getIsFilledProperty() {
        return isFilledProperty;
    }
}
