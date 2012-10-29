package macgyvers.mds04;

import java.io.*;
 
public class ServerRunner {
    public static void main(String[] args) throws IOException {
        new UDPServer().start();
    }
}

