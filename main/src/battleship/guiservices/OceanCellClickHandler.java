package battleship.guiservices;

import battleship.gui.IRefreshable;
import battleship.gui.RootPane;
import battleship.inner.IBattleshipGame;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Handler for Ocean Cell clicks
 */
public class OceanCellClickHandler implements EventHandler<MouseEvent> {
    private int row;
    private int col;
    private IBattleshipGame game;
    private IRefreshable<RootPane.RefreshValues> parent;

    /**
     * Constructs new instance of OcealCellClickHandler
     * @param row row of cell user will be clicking on
     * @param col column of cell user will be clicking on
     * @param game current game
     * @param parent instance will notify parent when will be clicked on
     */
    public OceanCellClickHandler(int row, int col, IBattleshipGame game, IRefreshable<RootPane.RefreshValues> parent) {
        this.row = row;
        this.col = col;
        this.game = game;
        this.parent = parent;
    }

    /**
     * Handles click mouse event
     * @param event mouse event
     */
    @Override
    public void handle(MouseEvent event) {
        game.shootAt(row, col);
        parent.refresh(RootPane.RefreshValues.ALL);
    }
}
