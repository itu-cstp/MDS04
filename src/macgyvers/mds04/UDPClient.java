package macgyvers.mds04;

import java.io.*;
import java.net.*;
import java.util.*;
 
public class UDPClient {
    public static void main(String[] args) throws IOException, InterruptedException {
    	boolean running = true;
    	//udp protocol over a socket.
    	DatagramSocket socket = new DatagramSocket();
        if (args.length < 1) {
             System.out.println("Usage: username command-id");
             socket.close();
             return;
        }
         // get a datagram socket

        byte[] byteArr = (args[0]+" "+args[1]).getBytes();

        // send request
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName("localhost");
        DatagramPacket packet = new DatagramPacket(byteArr, byteArr.length, address, 4445);
        socket.send(packet);

        // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        // display response
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("From server: " + received);

        socket.close();
    }
}
