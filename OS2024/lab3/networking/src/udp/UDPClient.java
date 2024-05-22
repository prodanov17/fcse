package udp;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread{
    private String serverName;
    private int serverPort;

    private DatagramSocket socket;
    private InetAddress address;
    private String message;
    private byte[] buffer;

    public UDPClient(String serverName, int serverPort, String message) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = message;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverName);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        buffer = message.getBytes();
        DatagramPacket packet;

        try {
            byte[] sendBuffer = message.getBytes();
            packet = new DatagramPacket(sendBuffer, sendBuffer.length, address, serverPort);
            socket.send(packet);

            byte[] receiveBuffer = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(receivedMessage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String serverName = System.getenv("SERVER_NAME");
        String serverPort = System.getenv("SERVER_PORT");
        UDPClient client = new UDPClient(serverName, Integer.parseInt(serverPort), "login");
        client.start();
    }


}
