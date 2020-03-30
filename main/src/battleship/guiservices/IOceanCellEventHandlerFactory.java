package battleship.guiservices;

import battleship.gui.IRefreshable;
import battleship.gui.RootPane;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Creates new Event handler for clicking on cell
 */
public interface IOceanCellEventHandlerFactory {
    public EventHandler<MouseEvent> getOceanCellEventHandler(int row, int col, IRefreshable<RootPane.RefreshValues> parent);
}
