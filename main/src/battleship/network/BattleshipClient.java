package battleship.network;

import battleship.network.dto.GreetingsDto;
import battleship.network.dto.IOnConnectionHandler;
import battleship.network.dto.ITypedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BattleshipClient implements IClient {
    private Socket clientSocket;
    private Optional<ConnectionThread> connectionThread = Optional.empty();
    private ArrayList<ITypedDto> messagesQueue = new ArrayList<>();
    private List<IOnConnectionHandler> handlerList = new ArrayList<>();
    private Assembly assembly;

    public BattleshipClient(Assembly assembly) {
        this.assembly = assembly;
    }

    @Override
    public void addOnConnectionHandler(IOnConnectionHandler handler) {
        handlerList.add(handler);
    }

    @Override
    public void runClientServer(String host, int port) {
        new Thread(() -> {
            try {
                this.clientSocket = new Socket(host, port);
                this.connectionThread = Optional.of(new ConnectionThread(clientSocket, assembly.getDtoReader()));
                this.connectionThread.get().start();
                flush();
                handlerList.forEach(IOnConnectionHandler::onSuccessfulConnection);
            } catch (IOException e) {
                e.printStackTrace();
                handlerList.forEach(el -> el.onFailedConnection(e.getMessage()));
            }
        }).start();
    }

    private void flush() {
        if (connectionThread.isPresent()) {
            var objectMapper = new ObjectMapper();

            for (var el : messagesQueue) {
                try {
                    connectionThread.get().write(objectMapper.writeValueAsString(el));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            messagesQueue.clear();
        }
    }

    @Override
    public void write(ITypedDto dto) {
        if (connectionThread.isPresent()) {
            messagesQueue.add(dto);
            flush();
        } else {
            messagesQueue.add(dto);
        }
    }

    public static void main(String[] args) throws IOException {
        Assembly assembly = new Assembly();

        var server = new BattleshipServer(assembly);
        server.runClientServer(8080);
        server.write(new GreetingsDto("Hello, I am server"));

        var client = new BattleshipClient(assembly);
        client.runClientServer("localhost", 8080);
        client.write(new GreetingsDto("Hello, I am client"));
    }
}
