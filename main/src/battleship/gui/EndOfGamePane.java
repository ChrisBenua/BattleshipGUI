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
     * @param onEndGame callback for quitting game
     */
    public EndOfGamePane(String winner, String playerName, String opponentName, int myStepsCount, int enemyStepsCount, Runnable onEndGame) {
        String message = String.format("Победитель -- %s", winner);
        Label label = new Label(message);
        label.setTextAlignment(TextAlignment.CENTER);

        var myStepsLabel = new Label(String.format("%s (Вы) сделали %d %s", playerName, myStepsCount, conjugate(myStepsCount)));
        var enemyStepsLabel = new Label(String.format("%s (Оппонент) сделал %d %s", opponentName, enemyStepsCount, conjugate(enemyStepsCount)));

        Button quitButton = new Button("Выйти");
        quitButton.setMaxWidth(Double.MAX_VALUE);


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

        for (int i = 0; i < 4; ++i) {
            var rowConstraint = new RowConstraints();
            rowConstraint.setValignment(VPos.CENTER);
            rowConstraint.setPercentHeight(100. / 2);
            this.getRowConstraints().add(rowConstraint);
        }

        this.add(label,0, 0, 5, 1);
        this.add(myStepsLabel, 0, 1, 5, 1);
        this.add(enemyStepsLabel, 0, 2, 5, 1);
        this.add(quitButton, 2, 3);
    }

    private String conjugate(int shotsCount) {
        var ending = "Выстрелов";
        if (shotsCount % 10 == 1 && ((shotsCount / 10) % 10) != 1) {
            ending = "Выстрел";
        } else if (shotsCount % 10 >= 2 && shotsCount % 10 < 5 && ((shotsCount / 10) % 10) != 1) {
            ending = "Выстрела";
        }

        return ending;
    }
}
