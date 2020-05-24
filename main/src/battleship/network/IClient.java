package battleship.network;

import java.io.IOException;

/**
 * Interface for defining client side logic connection to server
 */
public interface IClient extends IClientServer {
    /**
     * Establishes connection with given host
     * @param host host
     * @param port connection port
     * @throws IOException if socket creation failed
     */
    void runClientServer(String host, int port) throws IOException;
}
