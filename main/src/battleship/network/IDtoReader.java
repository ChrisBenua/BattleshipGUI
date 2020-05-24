package battleship.network;

/**
 * Interface for reading DTOs from socket connection
 */
public interface IDtoReader {
    /**
     * Adds new event handler for reading events
     * @param handler handler for reading events
     */
    void addEventsHandler(IDtoEventsHandler handler);

    /**
     * Removes new event handler
     * @param handler handler for reading events
     */
    void removeEventsHandler(IDtoEventsHandler handler);

    /**
     * Reads object from string
     * @param object String representation of object
     */
    void read(String object);
}
