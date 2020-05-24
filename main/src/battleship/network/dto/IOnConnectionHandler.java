package battleship.network.dto;

/**
 * Interface for handling connection events
 */
public interface IOnConnectionHandler {
    /**
     * Notifies about successful connection
     */
    void onSuccessfulConnection();

    /**
     * Notifies about failed connection
     * @param error error
     */
    void onFailedConnection(String error);
}
