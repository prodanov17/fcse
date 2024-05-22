package tcp;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean loggedIn;
    private static Lock mutex = new ReentrantLock();
    private static int messageCounter = 0;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.loggedIn = false;
    }

    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            readMessages();
        } catch (SocketException e) {
            System.out.println("Client disconnected: " + clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessages() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String receivedMessage;

            while ((receivedMessage = reader.readLine()) != null) {
                handleClientRequest(receivedMessage.trim());
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientRequest(String receivedMessage) {
        mutex.lock();
        try {
            messageCounter++;
            System.out.printf("Messages so far: %d\n", messageCounter);
        } finally {
            mutex.unlock();
        }

        try {
            if (!loggedIn && !receivedMessage.equals("login")) {
                clientSocket.close();
                return;
            }

            if (receivedMessage.equals("login")) {
                this.loggedIn = true;
                this.sendMessage("logged in");
            } else if (receivedMessage.equals("logout")) {
                this.loggedIn = false;
                this.sendMessage("logged out");
                clientSocket.close();
            } else {
                String response = "echo-" + receivedMessage;
                this.sendMessage(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(String message) {
        try {
            message = message.trim() + "\n";
            outputStream.write(message.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}