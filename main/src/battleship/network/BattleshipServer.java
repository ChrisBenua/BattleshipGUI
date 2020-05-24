package battleship.network;

import battleship.network.dto.IOnConnectionHandler;
import battleship.network.dto.ITypedDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class for creating TCP/IP socket server
 */
public class BattleshipServer implements IServer {
    /**
     * Stores status of connection
     */
    private AtomicBoolean isBusy;
    /**
     * Server socket
     */
    private ServerSocket serverSocket;
    /**
     * Connection thread to receive messages
     */
    private Optional<ConnectionThread> connectionThread = Optional.empty();
    /**
     * Queue of not sent messages
     */
    private ArrayList<ITypedDto> messagesQueue = new ArrayList<>();
    /**
     * List of successful connection listeners
     */
    private List<IOnConnectionHandler> handlerList = new ArrayList<>();
    private Assembly assembly;

    public BattleshipServer(Assembly assembly) {
        isBusy = new AtomicBoolean(false);
        this.assembly = assembly;
    }

    @Override
    public void addOnConnectionHandler(IOnConnectionHandler handler) {
        if (!handlerList.contains(handler)) {
            handlerList.add(handler);
        }
    }

    @Override
    public void close() {
        this.connectionThread.ifPresent(ConnectionThread::deactivate);
    }

    public void setBusy(boolean isBusy) {
        if (isBusy && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.isBusy.set(isBusy);
    }

    /**
     * Flushes messages to socket connection
     */
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

    public void runClientServer(int port) throws IOException {

        this.serverSocket = new ServerSocket(port);

        new Thread(() -> {
            try {
                var socket = serverSocket.accept();
                handlerList.forEach(IOnConnectionHandler::onSuccessfulConnection);
                setBusy(true);
                connectionThread = Optional.of(new ConnectionThread(socket, assembly.getDtoReader()));
                connectionThread.get().start();
                flush();
            } catch (IOException e) {
                e.printStackTrace();
                handlerList.forEach(el -> el.onFailedConnection(e.getMessage()));
            }
        }).start();
    }
}
