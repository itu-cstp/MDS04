package macgyvers.mds04;

import java.io.*;
import java.net.*;
import java.util.*;

import macgyvers.mds04.xml.Task;

public class UDPServer extends Thread {
	 
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected boolean looping = true;
    
    protected String user = null;
    protected String command = null;
    protected String id = null;
    protected String idNumber = null;
    protected String name = null;
    protected TaskType type = null;
    private TaskHandler handler = TaskHandler.getInstance();
    
    public UDPServer() throws IOException {
    	this("ServerThread");
    }
 
    public UDPServer(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);
    }
 
    public void run() {
        while (looping) {
            try {
                byte[] buf = new byte[256];
                
                String returnMessage = null;
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
                	command = inputs[1]; //the command is currently not used, but can be implemented to support different commands.
                	id = inputs[2];
                	//splitting the ID into type and id-number
                	String[] strArr = inputs[2].split("-");
                	if(strArr.length != 2) returnMessage = "please enter a valid assignment type and id, eg. handin-01";
                	else {
                		idNumber = strArr[1].trim();
                		type = TaskType.getType(strArr[0].trim().toLowerCase());
                	}
                }
                
                if(returnMessage == null){
	               // creates a new job and adds the relevant dependencies from the enum + timestamp etc..
	               Task task = new Task(id, new Date(), idNumber);
	               // adding the enum conditions
	               task.conditions = type.conditions;
	               //submit it for execution
                	handler.submitTask(task);
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
