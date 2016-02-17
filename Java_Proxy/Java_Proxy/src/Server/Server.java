package Server;
import java.awt.List;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

import java.net.HttpURLConnection;

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
		while(true) {
			try {
		    	System.out.println("Listening for client requests...");
		        clientSocket = serverSocket.accept();
		        Thread clientThread = new ClientSocketThread(clientSocket);
		        clientThread.start();
			} finally {
				
			}
		}
	}
}

class ClientSocketThread extends Thread {
	Socket clientSocket;
	public ClientSocketThread(Socket clientSocket) throws Exception {
		this.clientSocket = clientSocket;
		System.out.println("Successfully connected request on..." + clientSocket.getPort());
		
		try
		{
		    char[] buffer = new char[2048];
		    int charsRead = 0;
		    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		    String queryRequest = null;
		    boolean queryDetected = false;
		    while ((charsRead = in.read(buffer)) != -1)
		    {
		        String message = new String(buffer).substring(0, charsRead);
		        if(message.contains("query=")) {
		        	System.out.println("Query detected!");
		        	System.out.println(message.substring(message.indexOf("query="), message.length()));
		        	queryRequest = message.substring(message.indexOf("query="), message.length());
		        	System.out.println(queryRequest.substring(6, queryRequest.length() - 25));
		        	queryDetected = false; //Only false for debugging purposes
		        }
		    }
		    if(queryDetected) {
		    	//HTTP GET occurs
		    	
		    	//URL obj = new URL(queryRequest.substring(7, queryRequest.indexOf("=") - 25));
				//HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				
				// optional default is GET
				//con.setRequestMethod("GET");
		    	
		    	
		    	//Check to see if currently connected page was recently visited,
		    	//if not, store on the proxy host.
		    }
		
		} finally {
			clientSocket.close();
		}
	}
}

class WebpageObject {
	private String url;
	private String title;
	private String contents;

	public WebpageObject(String url, String title, String contents) {
		this.url = url;
		this.title = title;
		this.contents = contents;
	}
}