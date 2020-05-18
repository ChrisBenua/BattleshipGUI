package battleship.network;

import java.io.IOException;

public interface IServer extends IClientServer {
    void runClientServer(int port) throws IOException;
}
