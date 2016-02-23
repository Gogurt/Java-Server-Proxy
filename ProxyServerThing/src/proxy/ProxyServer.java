package proxy;

import java.net.*;
import java.io.*;
import java.nio.*;

        

public class ProxyServer {
    public static void main(String[] args) throws IOException {
        
        //Create a webpage cache object
        SimpleCache<InputStream> webCache = new SimpleCache<InputStream>();
        
        ServerSocket serverSocket = null;
        boolean listening = true;

        int port = 555;	//default
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            //ignore me
        }

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started on: " + port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + args[0]);
            System.exit(-1);
        }

        while (listening) {
            new ProxyThread(serverSocket.accept(), webCache).start();
        }
        serverSocket.close();
    }
}
