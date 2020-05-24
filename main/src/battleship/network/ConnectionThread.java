package battleship.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Thread for receiving messages from sockets
 */
public class ConnectionThread extends Thread {
    /**
     * Connection socket
     */
    private Socket socket;
    /**
     * Is connection active
     */
    private boolean isActive = true;
    /**
     * Writer
     */
    private PrintStream outputStreamWriter;
    /**
     * DTO reader
     */
    private IDtoReader reader;

    public ConnectionThread(Socket socket, IDtoReader reader) throws IOException {
        this.socket = socket;
        outputStreamWriter = new PrintStream(socket.getOutputStream());
        this.reader = reader;
        this.setDaemon(true);
    }

    /**
     * Closes socket
     */
    public void deactivate() {
        isActive = false;
        try {
            socket.close();
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes object to socket
     * @param object string to write
     */
    public void write(String object) {
        outputStreamWriter.println(object);
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                var bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var jsonObject = bufferedReader.readLine();

                if (jsonObject == null) {
                    deactivate();
                } else {
                    reader.read(jsonObject);
                    System.out.println(jsonObject);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
