package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<Worker> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            String port = System.getenv("SERVER_PORT");
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port));
            System.out.println("Server is running and waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create a new thread to handle the client
                Worker clientHandler = new Worker(clientSocket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void broadcastMessage(String message, Worker sender) {
//        for (Worker client : clients) {
//            if (client != sender) {
//                client.sendMessage("Client " + sender.getClientId() + ": " + message);
//            }
//        }
//    }
}
