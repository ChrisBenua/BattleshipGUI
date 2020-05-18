package battleship.network.dto;

public interface IOnConnectionHandler {
    void onSuccessfulConnection();

    void onFailedConnection(String error);
}
