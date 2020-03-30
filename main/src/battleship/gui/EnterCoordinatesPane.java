package battleship.gui;

import battleship.inner.BattleshipGame;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.function.BiConsumer;

/**
 * Dialog for entering shot coordinates without mouse.
 * Use tab for navigating
 */
public class EnterCoordinatesPane extends BorderPane {

    private TextField rowTextField;
    private TextField colTextField;

    private BiConsumer<Integer, Integer> onSuccessfulCoordsEnter;

    /**
     * Process and validates entered coordinates
     */
    private void processCoordinates() {
        try {
            int row = Integer.parseInt(rowTextField.getText());
            int col = Integer.parseInt(colTextField.getText());

            if (row < 0 || row >= BattleshipGame.OCEAN_SIZE || col < 0 || col >= BattleshipGame.OCEAN_SIZE) {
                NotificationManager.showNotification("Invalid range of row and column", 1500, this);
            } else {
                onSuccessfulCoordsEnter.accept(row, col);
                this.getScene().getWindow().hide();
            }
        } catch (NumberFormatException ex) {
            NotificationManager.showNotification("Invalid number format of row and column", 1500, this);
        }
    }

    /**
     * Constructs new instance of EnterCoordinatesPane with success callback
     * @param onSuccessfulCoordsEnter callback on entering valid coordinates
     */
    public EnterCoordinatesPane(BiConsumer<Integer, Integer> onSuccessfulCoordsEnter) {
        this.onSuccessfulCoordsEnter = onSuccessfulCoordsEnter;
        rowTextField = new TextField();
        rowTextField.setPromptText("Row(0 - 9)");

        colTextField = new TextField();
        colTextField.setPromptText("Column(0 - 9)");
        var button = new Button("Submit");

        button.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                processCoordinates();
            }
        });

        button.setOnMouseClicked(ev -> {
            processCoordinates();
        });

        VBox vBox = new VBox(20, rowTextField, colTextField, button);
        vBox.setAlignment(Pos.CENTER);
        this.setCenter(vBox);
    }
}
