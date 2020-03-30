package battleship.gui;

import battleship.inner.ISubscriber;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * Container for displaying game logs
 */
public class EventLogsContainer extends AnchorPane implements ISubscriber<String> {
    private String logs = "";
    private TextArea textArea;

    public EventLogsContainer() {
        textArea = new TextArea();
        textArea.setEditable(false);

        AnchorPane.setRightAnchor(textArea, 0.);
        AnchorPane.setLeftAnchor(textArea, 0.);
        AnchorPane.setTopAnchor(textArea, 0.);
        AnchorPane.setBottomAnchor(textArea, 0.);

        this.getChildren().addAll(textArea);
    }

    /**
     * Accepts new log entry
     * @param newVal new log
     */
    @Override
    public void accept(String newVal) {
        logs = newVal + '\n' + logs;
        textArea.setText(logs);
    }
}
