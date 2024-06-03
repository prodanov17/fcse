package filetransfer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;

public class Worker extends Thread{
    private Socket clientSocket;
    private BufferedReader inputStream;
    private BufferedWriter outputStream;

    private static final String DIRECTORY_PATH = "src/filetransfer/files";

    private File directory = new File(DIRECTORY_PATH);

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("New client connected");
        try {
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputStream = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            readMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(clientSocket != null)
            {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null)
            {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleCommand(String command){
        if(command.equals("exit")){
            System.out.println("Client disconnected");
        }
        else if(command.equals("listFiles")){

            Arrays.stream(directory.listFiles()).forEach(file -> {
                sendMessage(file.getName());
            });
        }
        else if(command.startsWith("downloadfile?")){
            String[] parts = command.split("\\?");
            String fileName = parts[1].split("=")[1];
            File file = new File(DIRECTORY_PATH + "/" + fileName);
            if(file.exists()){
                try{
                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                    String line;
                    sendMessage("downloadfile:start?name=" + file.getName());
                    while((line = fileReader.readLine()) != null){
                        sendMessage(line);
                        Thread.sleep(1000);
                    }
                    sendMessage("downloadfile:end");
                    fileReader.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                sendMessage("File not found");
            }
        }
        else{
            System.out.println("Unknown command");
        }
    }

    private void readMessages() throws IOException {
        try {
            String line;

            while((line = inputStream.readLine()) != null){
                System.out.println("Client: " + line);
                handleCommand(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            inputStream.close();
        }
    }

    public void sendMessage(String message){
        try
        {
            outputStream.write(message);
            outputStream.newLine();
            outputStream.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
