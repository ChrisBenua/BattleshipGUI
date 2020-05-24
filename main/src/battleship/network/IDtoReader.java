package battleship.network;

public interface IDtoReader {
    void addEventsHandler(IDtoEventsHandler handler);

    void removeEventsHandler(IDtoEventsHandler handler);

    void read(String object);
}
