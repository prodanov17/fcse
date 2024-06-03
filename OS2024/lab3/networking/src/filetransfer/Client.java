package filetransfer;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class Client extends Thread{
    private BufferedReader inputBuffer;
    private BufferedWriter outputBuffer;
    private File file;
    private static String SAVE_DIR = "src/filetransfer/clientfiles/";

    private boolean isDownloading = false;

    private int serverPort;
    private String serverName;

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

            sendMessage("listFiles");
            new Thread(() -> {
                try {
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            String command;
            System.out.println("Enter commands (type 'exit' to quit):");

            while (true) {
                System.out.print("> ");
                command = scanner.nextLine();

                if (command.equalsIgnoreCase("exit")) {
                    break;
                }

                sendMessage(command);
            }

            scanner.close();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if(inputBuffer != null) {
                try {
                    inputBuffer.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(outputBuffer != null) {
                try {
                    outputBuffer.flush();
                    outputBuffer.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleMessage(String message){
        if(message.startsWith("downloadfile:start?")){
            isDownloading = true;
            file = new File(SAVE_DIR + "downloaded_" + message.split("=")[1]);
        }
        else if(message.equalsIgnoreCase("downloadfile:end")){
            isDownloading = false;
        }
        else if(isDownloading){
            System.out.println(message);
            try{
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file, true));
                fileWriter.write(message);
                fileWriter.newLine();
                fileWriter.flush();
                fileWriter.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println(message);
        }
    }

	private void readMessages() throws IOException {
        String line;
        while((line = inputBuffer.readLine()) != null){
            handleMessage(line);
        }
    }

    private void sendMessage(String message) throws IOException {
        outputBuffer.write(message.trim());
        outputBuffer.newLine();
        outputBuffer.flush();
    }

    public static void main(String[] args) {
        String serverName = "localhost";
        int serverPort = 8000;
        Client client = new Client(serverName, serverPort);
        client.start();
    }
}
