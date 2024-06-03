package tcp2;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    private static String FILENAME = System.getenv("COUNTER_FILE");
    private Socket clientSocket;
    private BufferedReader inputStream;
    private BufferedWriter outputStream;
    private RandomAccessFile rafCounter;
    private boolean loggedIn;
    private static Lock mutex = new ReentrantLock();

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.loggedIn = false;
    }

    @Override
    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputStream = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            rafCounter = new RandomAccessFile(new File(FILENAME), "rw");
//            incrementCounter();

            readMessages();
        } catch (SocketException e) {
            System.out.println("Client disconnected: " + clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                inputStream.close();
                outputStream.close();
                rafCounter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readMessages() throws IOException {
        try {
            String receivedMessage;

            while ((receivedMessage = inputStream.readLine()) != null) {
                handleClientRequest(receivedMessage.trim());
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            inputStream.close();
        }
    }

    private Integer incrementCounter() throws IOException {
        mutex.lock();
        Integer counter = 0;
        try{
            try {
                rafCounter.seek(0);
                counter = this.rafCounter.readInt();
            }
            catch (IOException ex){
                this.rafCounter.seek(0);
                this.rafCounter.writeInt(0);
            }
            counter++;

            this.rafCounter.seek(0);
            this.rafCounter.writeInt(counter);
        }
        finally {
            mutex.unlock();
        }
        return counter;
    }

    private void handleClientRequest(String receivedMessage) {
        try {
            Integer messageCounter = incrementCounter();
            System.out.printf("Messages so far: %d\n", messageCounter);
        } catch (IOException ex) {
            ex.printStackTrace();
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
            outputStream.write(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}