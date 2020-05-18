package battleship.network;

import java.io.IOException;

public interface IClient extends IClientServer {
    void runClientServer(String host, int port) throws IOException;
}
