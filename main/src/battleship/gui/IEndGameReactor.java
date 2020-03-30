package battleship.gui;

/**
 * Interface for ending game using kind of event handling
 */
interface IEndGameReactor {
    /**
     * Event handler for restarting game
     */
    void restart();

    /**
     * Event handler for quitting game
     */
    void quit();
}
