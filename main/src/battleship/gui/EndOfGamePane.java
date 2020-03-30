package battleship.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;

/**
 * Dialog for starting new game or quitting app
 */
public class EndOfGamePane extends GridPane {
    /**
     * Construct new Instance of EndOfGamePane
     * @param hitCount number of hits in ended game
     * @param onStartNewGame callback for starting new game
     * @param onEndGame callback for quitting game
     */
    public EndOfGamePane(int hitCount, Runnable onStartNewGame, Runnable onEndGame) {
        String message = "Congrats! You won!\nIt took you " + String.valueOf(hitCount) +
                " shots to complete! Nice!";
        Label label = new Label(message);
        label.setTextAlignment(TextAlignment.CENTER);
        Button okButton = new Button("New game");
        okButton.setMaxWidth(Double.MAX_VALUE);
        Button quitButton = new Button("Quit");
        quitButton.setMaxWidth(Double.MAX_VALUE);

        okButton.setOnMouseClicked(event -> {
            this.getScene().getWindow().hide();
            onStartNewGame.run();
        });

        quitButton.setOnMouseClicked(event -> {
            this.getScene().getWindow().hide();
            onEndGame.run();
        });

        for (int i = 0; i < 5; ++i) {
            var columnConstraint = new ColumnConstraints();
            columnConstraint.setHalignment(HPos.CENTER);
            columnConstraint.setHgrow(Priority.ALWAYS);
            columnConstraint.setPercentWidth(100. / 5);
            this.getColumnConstraints().add(columnConstraint);
        }

        for (int i = 0; i < 2; ++i) {
            var rowConstraint = new RowConstraints();
            rowConstraint.setValignment(VPos.CENTER);
            rowConstraint.setPercentHeight(100. / 2);
            this.getRowConstraints().add(rowConstraint);
        }

        this.add(label,0, 0, 5, 1);
        this.add(okButton, 1, 1);
        this.add(quitButton, 3, 1);
    }
}
