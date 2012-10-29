package macgyvers.mds04;

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer extends Thread {
	 
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean looping = true;
    
    protected String user = null;
    protected String command = null;
    protected int id = 0;
    
    public UDPServer() throws IOException {
    	this("QuoteServerThread");
    }
 
    public UDPServer(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);
    }
 
    public void run() {
        while (looping) {
            try {
                byte[] buf = new byte[256];
                
                String returnMessage = "";
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                
                if (packet.getData() == null)
                    returnMessage = "Usage: user command id";
                
                String input = new String(packet.getData(), 0, packet.getLength());
                
                String[] inputs = input.split(" ");
                
                if(inputs.length < 3)
                	returnMessage = "Usage: user command id";
                else {
                	user = inputs[0];
                	command = inputs[1];
                	try{
                		id = Integer.parseInt(input);
                	}catch(NumberFormatException nfe){
                		returnMessage = "Usage: user command (int)id";
                	}
                }
                if(returnMessage == ""){
	               // String dString =TaskHandler.handle(user,command,id);
	               
                }
                buf = returnMessage.getBytes();

                // send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
