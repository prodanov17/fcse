package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Client extends Thread {
    private int serverPort;
    private String serverName;

    public Client(String serverName, int serverPort){
        this.serverPort = serverPort;
        this.serverName = serverName;
    }

    @Override
    public void run(){
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        Random random = new Random();

        try{
            socket = new Socket(InetAddress.getByName(this.serverName), this.serverPort);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write("login\n");
            writer.flush();

            writer.write("las\n");
            writer.flush();

            if(random.nextBoolean()) {
                writer.write("logout\n");
                writer.flush();
            }

            readMessage(reader);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null){
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readMessage(BufferedReader reader) throws IOException {
        String line;
        while((line = reader.readLine()) != null){
            System.out.println(line);
        }
    }

    public static void main(String[] args){
        String serverName = System.getenv("SERVER_NAME");
        String serverPort = System.getenv("SERVER_PORT");
//        String serverName = "localhost";
//        String serverPort = "8111";
        if (serverPort == null){
            throw new RuntimeException("Server port should be defined as ENV {SERVER_PORT}.");
        }

        Client client = new Client(serverName, Integer.parseInt(serverPort));
        client.start();
    }
}
