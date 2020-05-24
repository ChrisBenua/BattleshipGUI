package battleship.network;

import battleship.network.dto.IOnConnectionHandler;
import battleship.network.dto.ITypedDto;

/**
 * Defines common interface for interacting with client and server side
 */
public interface IClientServer {
    /**
     * Writes given instance to socket connection
     * @param dto message instance
     */
    void write(ITypedDto dto);

    /**
     * Adds connection handler
     * @param handler gets notified when connection was established
     */
    void addOnConnectionHandler(IOnConnectionHandler handler);

    /**
     * Closes connection
     */
    void close();
}
