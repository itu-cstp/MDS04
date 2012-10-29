package macgyvers.mds04;

import java.io.*;
import java.net.*;
import java.util.*;
 
public class UDPClient {
    public static void main(String[] args) throws IOException, InterruptedException {
    	boolean running = true;
    	DatagramSocket socket = new DatagramSocket();
        if (args.length != 1) {
             System.out.println("Usage: java QuoteClient <hostname>");
             return;
        }
        while(running){
	         // get a datagram socket
	        
	        byte[] test = "sdad".getBytes();        
	        
	        // send request
	        byte[] buf = new byte[256];
	        InetAddress address = InetAddress.getByName(args[0]);
	        DatagramPacket packet = new DatagramPacket(test, test.length, address, 4445);
	        socket.send(packet);
	     
	            // get response
	        packet = new DatagramPacket(buf, buf.length);
	        socket.receive(packet);
	 
	        // display response
	        String received = new String(packet.getData(), 0, packet.getLength());
	        System.out.println("From server: " + received);
	        Thread.sleep(2000);
        }
        socket.close();
    }
}
