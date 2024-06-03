package shared;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread{
    int serverPort;
    String serverName;
    BufferedReader inputBuffer;
    BufferedWriter outputBuffer;

    public Client(String serverName, int serverPort) {
        this.serverPort = serverPort;
        this.serverName = serverName;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(serverName), serverPort);
            this.inputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputBuffer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            outputBuffer.write("Hello world");
            outputBuffer.flush();
            readMessages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(inputBuffer != null) {
                try {
                    inputBuffer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(outputBuffer != null) {
                try {
                    outputBuffer.flush();
                    outputBuffer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void readMessages() throws IOException {
        String line;
        while((line = inputBuffer.readLine()) != null){
            System.out.println(line);
        }
    }

    public static void main(String[] args){
        String serverName = "localhost";
        String serverPort = "8001";

        Client client = new Client(serverName, Integer.parseInt(serverPort));
        client.start();
    }
}
