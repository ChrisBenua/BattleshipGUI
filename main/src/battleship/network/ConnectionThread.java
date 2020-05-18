package battleship.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ConnectionThread extends Thread {
    private Socket socket;
    private boolean isActive = true;
    private PrintStream outputStreamWriter;
    private IDtoReader reader;

    public ConnectionThread(Socket socket, IDtoReader reader) throws IOException {
        this.socket = socket;
        outputStreamWriter = new PrintStream(socket.getOutputStream());
        this.reader = reader;
    }

    public void deactivate() {
        isActive = false;
        try {
            socket.close();
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String object) {
        outputStreamWriter.println(object);
    }

    @Override
    public void run() {
        while (isActive) {
            try {
                var bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var jsonObject = bufferedReader.readLine();
                reader.read(jsonObject);
                System.out.println(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
