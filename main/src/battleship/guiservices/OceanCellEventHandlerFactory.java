package battleship.guiservices;

import battleship.gui.IRefreshable;
import battleship.gui.RootPane;
import battleship.inner.IBattleshipGame;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Class for creating clickEventHandlers for Ocean Cells
 */
public class OceanCellEventHandlerFactory implements IOceanCellEventHandlerFactory {
    private IBattleshipGame game;

    /**
     * Constructs new instance of factory
     * @param game current Battleship game
     */
    public OceanCellEventHandlerFactory(IBattleshipGame game) {
        this.game = game;
    }

    /**
     * creates new Ocean Cell event handler
     * @param row row of current cell
     * @param col column of current cell
     * @param parent parent to be notified on click
     * @return EventHandler realization
     */
    @Override
    public EventHandler<MouseEvent> getOceanCellEventHandler(int row, int col, IRefreshable<RootPane.RefreshValues> parent) {
        return new OceanCellClickHandler(row, col, game, parent);
    }
}
