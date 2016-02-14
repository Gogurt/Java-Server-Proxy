package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/* Simple Java Proxy
 * Listens for additional connecting clients,
 * creates multithreaded socket connections,
 * sends and receives requested information
 * from connected clients, keeps a cache
 * of recently accessed webpages, and logs
 * all activity while doing so.
 */
public class Server {
	
	public static int portNumber = 555;
	
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.runServer();
		
		System.out.println("Finished!");
	}

	public static void runServer() throws Exception {
		
		System.out.println("Starting server...");
		ServerSocket serverSocket;
		Socket clientSocket;
		
		serverSocket = new ServerSocket(portNumber);
		System.out.println("Successfully created server socket");
		try {
		    while (true) { 
		    	System.out.println("Listening for clients...");
		        clientSocket = serverSocket.accept();
		        /* Issue occurs where the same client will
		         * continuously connect with various ports
		         * through the .accept method. Need some
		         * conditional to verify the client doesn't
		         * need to connect more than once.
		         */
		        
		        Thread clientThread = new ClientSocketThread(clientSocket);
		        clientThread.start();
		    }
		} finally {
		    serverSocket.close();
		}
		
	}
}

class ClientSocketThread extends Thread {
	Socket clientSocket;
	public ClientSocketThread(Socket clientSocket) throws Exception {
		this.clientSocket = clientSocket;
		System.out.println("Successfully connected on..." + clientSocket.getPort());
		
		//This is where the thread will listen to the connected client
		//in order to send and receive web page information,
		//as well as verifying/adding the webpage as a cache object.
	}
}