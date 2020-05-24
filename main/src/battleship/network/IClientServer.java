package battleship.network;

import battleship.network.dto.IOnConnectionHandler;
import battleship.network.dto.ITypedDto;

public interface IClientServer {
    void write(ITypedDto dto);

    void addOnConnectionHandler(IOnConnectionHandler handler);

    void close();
}
