package battleship.network;

public interface IDtoReader {
    void addEventsHandler(IDtoReaderEventsHandler handler);

    void read(String object);
}
