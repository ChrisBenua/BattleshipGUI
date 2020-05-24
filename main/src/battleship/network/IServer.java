package battleship.network;

import java.io.IOException;

/**
 * Interface for running TCP/IP server
 */
public interface IServer extends IClientServer {
    /**
     * Launches TCP/IP server
     * @param port port
     * @throws IOException exception running server socket
     */
    void runClientServer(int port) throws IOException;
}
