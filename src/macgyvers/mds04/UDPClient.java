package macgyvers.mds04;

import java.io.*;
import java.net.*;
import java.util.*;
 
public class UDPClient {
    public static void main(String[] args) throws IOException, InterruptedException {
    	boolean running = true;
    	DatagramSocket socket = new DatagramSocket();
        if (args.length < 1) {
             System.out.println("Usage: java QuoteClient <hostname>");
             socket.close();
             return;
        }
        while(running){
	         // get a datagram socket
	        
	        byte[] byteArr = (args[0]+" "+args[1]+" "+args[2]).getBytes();        
	        
	        // send request
	        byte[] buf = new byte[256];
	        InetAddress address = InetAddress.getByName("ServerThread"); //this line fails... cant remember what was in the example code.. easy fix though
	        DatagramPacket packet = new DatagramPacket(byteArr, byteArr.length, address, 4445);
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
