package battleship.gui;

/**
 * Interface for ending game using kind of event handling
 */
interface IEndGameReactor {
    /**
     * Cancel game
     */
    void cancel();

    /**
     * Event handler for quitting game
     */
    void quit();
}
