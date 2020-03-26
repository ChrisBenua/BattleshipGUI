package battleship.gui;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.YELLOW;

public class OceanCell extends AnchorPane {
    public OceanCell(double width, double height) {
        Rectangle border = new Rectangle(width, height);
        border.setFill(YELLOW);
        border.setStroke(BLACK);
        border.setStrokeWidth(1);

        AnchorPane.setBottomAnchor(border, 0.);
        AnchorPane.setTopAnchor(border, 0.);
        AnchorPane.setLeftAnchor(border, 0.);
        AnchorPane.setRightAnchor(border, 0.);

        this.getChildren().add(border);

    }
}
