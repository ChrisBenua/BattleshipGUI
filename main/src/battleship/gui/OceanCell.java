package battleship.gui;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.BLACK;

/**
 * Container for Ocean's cell
 */
public class OceanCell extends AnchorPane {
    /**
     * constructs new instance of Ocean Cell
     * @param width width of container
     * @param height height of container
     * @param fillColor fill color of container
     */
    public OceanCell(double width, double height, Color fillColor) {
        Rectangle border = new Rectangle(width, height);
        border.setFill(fillColor);
        border.setStroke(BLACK);
        border.setStrokeWidth(1);

        AnchorPane.setBottomAnchor(border, 0.);
        AnchorPane.setTopAnchor(border, 0.);
        AnchorPane.setLeftAnchor(border, 0.);
        AnchorPane.setRightAnchor(border, 0.);

        this.getChildren().add(border);
    }
}
