package battleship.gui;

import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for handling PlacingShipsOceanGridPane events
 */
interface IShipsContainerPaneEventHandler {
    /**
     * Notifies when drag was ended
     */
    void onDragDone();

    /**
     * Notifies when pane was cleared
     */
    void onClear();
}

public class ShipsContainerPane extends GridPane {
    private List<ShipData> ships = new ArrayList<>();
    private IShipsContainerPaneEventHandler handler;
    private Button clearButton;

    public ShipsContainerPane(
            IShipsContainerPaneEventHandler handler,
            BooleanBinding isPlayButtonEnabled,
            IShipsContainerButtonsClickHandler shipsContainerButtonsClickHandler,
            BooleanBinding isFindOpponentButtonEnabled
            ) {

        this.handler = handler;
        for (int i = 0; i < 9; ++i) {
            var rowConstraint = new RowConstraints();
            rowConstraint.setVgrow(Priority.NEVER);

            this.getRowConstraints().add(rowConstraint);
        }
        var rowConstraint = new RowConstraints();
        rowConstraint.setVgrow(Priority.ALWAYS);
        this.getRowConstraints().add(rowConstraint);

        placeShipsRectangles();

        var allocateButton = new Button("Расположить");
        clearButton = new Button("Очистить");
        var connectButton = new Button("Найти оппонента");
        var playButton = new Button("Играть");

        connectButton.disableProperty().bind(isFindOpponentButtonEnabled);

        connectButton.setOnAction(ev -> {
            shipsContainerButtonsClickHandler.onFindOpponentButtonClicked();
        });

        GridPane.setMargin(clearButton, new Insets(10, 0, 0, 0));
        GridPane.setMargin(connectButton, new Insets(10, 0, 0, 0));
        GridPane.setMargin(playButton, new Insets(10, 0, 0, 0));
        GridPane.setMargin(allocateButton, new Insets(5, 0, 0, 0));



        this.add(allocateButton, 0, 5);
        this.add(clearButton, 0, 6);
        this.add(connectButton, 0, 7);
        this.add(playButton, 0, 8);

        playButton.disableProperty().bind(isPlayButtonEnabled);
        playButton.setOnAction(ev -> {
            shipsContainerButtonsClickHandler.onPlayButtonClicked(null);
        });

        System.out.println(playButton.disableProperty().get());

        allocateButton.setOnAction(ev -> {
            activatePlacing();
            allocateButton.setDisable(true);
        });

        clearButton.setOnAction(ev -> {
            handler.onClear();
            placeShipsRectangles();
            activatePlacing();
        });
    }

    void disableClearButton() {
        clearButton.setDisable(true);
    }

    private void activatePlacing() {
        for (var ship : this.ships) {
            var shipRect = ship.getRectangle();

            if (ship.isHorizontal) {
                shipRect.getText().setText("H");
            } else {
                shipRect.getText().setText("V");
            }

            var hbox = (HBox)shipRect.getParent();

            shipRect.setOnDragDetected((event) -> {
                var dragBoard = shipRect.startDragAndDrop(TransferMode.MOVE);

                if (!ship.isHorizontal) {
                    shipRect.rectangle.setRotate(90);
                }
                dragBoard.setDragView(shipRect.rectangle.snapshot(null, null));
                shipRect.rectangle.setRotate(0);

                ClipboardContent content = new ClipboardContent();
                content.put(ShipsPlacingPane.shipsDataFormat, new ShipDragBoardDto(ship.getShipLength(), ship.isHorizontal));
                dragBoard.setContent(content);

                event.consume();
            });

            shipRect.setOnMouseClicked(event -> {
                ship.setHorizontal(!ship.isHorizontal());

                if (ship.isHorizontal) {
                    shipRect.getText().setText("H");
                } else {
                    shipRect.getText().setText("V");
                }
            });

            shipRect.setOnDragDone((event) -> {
                if (event.isAccepted()) {
                    hbox.getChildren().remove(shipRect);
                }

                handler.onDragDone();
            });
        }
    }

    private void placeShipsRectangles() {
        if (ships.isEmpty()) {
            for (int i = 0; i < 4; ++i) {
                int shipLen = 4 - i;
                int shipsCount = i + 1;

                for (int j = 0; j < shipsCount; ++j) {
                    var text = new Text("H");
                    text.setFont(Font.font(11));
                    var shipRect = new ShipRect(new Rectangle(), text);
                    ships.add(new ShipData(shipRect, shipLen));
                }
            }
        }
        int ind = 0;
        for (int i = 0; i < 4; ++i) {
            int shipLen = 4 - i;
            int shipsCount = i + 1;

            var hbox = new HBox(20);
            hbox.setMaxHeight(35);
            hbox.setMinHeight(35);

            for (int j = 0; j < shipsCount; ++j, ind++) {
                ships.get(ind).setHorizontal(true);
                var shipRect = ships.get(ind).getRectangle();


                shipRect.rectangle.heightProperty().bind(shipRect.heightProperty().subtract(10));
                shipRect.rectangle.widthProperty().bind(shipRect.rectangle.heightProperty().multiply(shipLen));
                shipRect.minHeight(20);

                shipRect.rectangle.setFill(Color.ALICEBLUE);
                shipRect.rectangle.setStrokeWidth(1);
                shipRect.rectangle.setStroke(Color.DODGERBLUE);

                hbox.getChildren().add(shipRect);
            }
            this.add(hbox, 0, i);
        }
    }

    private static class ShipRect extends StackPane {
        private Rectangle rectangle;
        private Text text;

        public ShipRect(Rectangle rectangle, Text text) {
            this.rectangle = rectangle;
            this.text = text;

            this.getChildren().addAll(rectangle, text);
        }

        public Rectangle getRectangle() {
            return rectangle;
        }

        public Text getText() {
            return text;
        }
    }

    private static class ShipData {
        private ShipRect rectangle;
        private int shipLength;
        private boolean isHorizontal = true;

        public ShipData(ShipRect rectangle, int shipLength) {
            this.rectangle = rectangle;
            this.shipLength = shipLength;
        }

        public ShipRect getRectangle() {
            return rectangle;
        }

        public int getShipLength() {
            return shipLength;
        }

        public boolean isHorizontal() {
            return isHorizontal;
        }

        public void setHorizontal(boolean horizontal) {
            isHorizontal = horizontal;
        }
    }
}
