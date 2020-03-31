package battleship.gui;

import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static javafx.scene.paint.Color.BLACK;

/**
 * Container for Ocean's cell
 */
public class OceanCell extends StackPane {
    private Rectangle border;
    /**
     * constructs new instance of Ocean Cell
     * @param fillColor fill color of container
     */
    public OceanCell(Color fillColor, NumberBinding binding) {
        Rectangle border = new Rectangle();
        this.border = border;
        border.setFill(fillColor);
        border.setStroke(BLACK);
        border.setStrokeWidth(1);

        border.widthProperty().bind(binding.subtract(1));
        border.heightProperty().bind(binding.subtract(1));

        this.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().add(border);
    }

    /**
     * updates fill color
     * @param fillColor new fill color
     */
    void updateFill(Color fillColor) {
        this.border.setFill(fillColor);
    }
}
