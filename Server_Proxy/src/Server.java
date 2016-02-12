/* Garrett Leatherman
 * 2/11/16
 * Simple HTTP Proxy
 * Proxy server that will allow multiple users to connect and
 * share a pool of recently accessed webpages.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class ThreadedConnection extends Thread {
	Socket clientSocket;
	
	public ThreadedConnection(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void start() {
		System.out.println("Starting up proxy server on port " + clientSocket.getPort());
	}
}

public class Server {
	public static void main(String[] args) {
		//int portNumber = Integer.parseInt(args[0]);
		
		System.out.println("Starting up proxy server...");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(555);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			System.out.println("Listening for connection...");
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Successfully connected user!");
				//Log connected user information
				
				//Starting new thread for client
				ThreadedConnection currentClient = new ThreadedConnection(clientSocket);
				currentClient.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//serverSocket.close();
		//System.out.println("Finished!");
	}
}
