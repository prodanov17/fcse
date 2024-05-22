package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread{
    private DatagramSocket socket;
    private byte[] buffer;

    public UDPServer(int port) {
        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.buffer = new byte[256];
    }

    @Override
    public void run(){
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while(true){
            try{
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("RECEIVED: " + received);

                String msg;
                if (received.equals("login")) {
                    msg = "logged in";
                }
                else if(received.equals("logout")){
                    msg = "logged out";
                }
                else {
                    msg = "echo-" + received;
                }

                // Create a new buffer for the response with the exact message length
                byte[] responseBuffer = msg.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, packet.getAddress(), packet.getPort());

                // Send the response
                socket.send(responsePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        String port = System.getenv("SERVER_PORT");
        UDPServer server = new UDPServer(Integer.parseInt(port));
        server.start();
    }

}
