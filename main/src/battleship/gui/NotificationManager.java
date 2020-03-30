package battleship.gui;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Class for displaying notification on the top of control's window
 */
public class NotificationManager {

    /**
     * Shows notification on the top of control's parent
     * @param message message inside notification
     * @param timeout time in millis before fade out
     * @param control focused control
     */
    public static void showNotification(String message, int timeout, Node control) {
        Stage stage = (Stage)control.getScene().getWindow();
        Popup popup = new Popup();
        popup.setAutoFix(true);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-background-color: cornsilk; -fx-padding: 10; -fx-border-color: darkgray;" +
                "-fx-border-width: 2; -fx-font-size: 16; -fx-border-radius: 10; -fx-background-radius: 10");

        popup.getContent().add(messageLabel);

        popup.setOnShown(event -> {
            popup.setX(stage.getX() + (stage.getWidth() - popup.getWidth()) / 2);//Center horizontally
            popup.setY(stage.getY());
        });

        popup.show(stage);
        var ft = new FadeTransition(Duration.seconds(0.8), messageLabel);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(el -> popup.hide());
        var timeline = new Timeline(new KeyFrame(Duration.millis(timeout), (event) -> {
            ft.play();
        }));

        timeline.play();
    }
}
