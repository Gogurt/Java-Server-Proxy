package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/* Java Server Proxy
 * A proxy that listens for connection clients,
 * passes their requests to their own thread,
 * processes their request and attempts to resolve
 * the clients requested url.
 */
public class Server {
	/*	Main
	 * 	Handles the constantly-listening server socket.
	 *  When a client is found, it gives it it's own socket
	 *  and a thread to process its request as the server
	 *  socket continues to listen for additional requests.
	 */
	public static LogIt logIt = new LogIt();
	
	public static void main(String[] args) {
		
		int serverPort = 555;
		
		System.out.println("Starting server...");
		
		ServerSocket serverSocket;
		Socket clientSocket;
		
		try {
			serverSocket = new ServerSocket(serverPort);
			System.out.println("Successfully created server socket on port " + serverPort);
			while(true) {
			    	System.out.println("Listening for client requests...");
			        clientSocket = serverSocket.accept();
			        System.out.println("Heard response from " + clientSocket.getRemoteSocketAddress().toString());
			        Thread clientThread = new ClientSocketThread(clientSocket);
			        clientThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

class ClientSocketThread extends Thread {
	public ClientSocketThread(Socket clientSocket) throws Exception {
		//for parsing URL
		String urlToCall = "";
		String token0 = "";
		int length;
		OutputStream outputStream = clientSocket.getOutputStream();
        InputStream inputStream = clientSocket.getInputStream();
		
		String response = null;
		// Logging object
		LogIt logger = new LogIt();
		
		try {
		    char[] buffer = new char[2048];
		    int charsRead = 0;
		    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		    
		    while ((charsRead = in.read(buffer)) != -1) {
		        String message = new String(buffer).substring(0, charsRead);
		        //parses URL!!
		        String[] tokens = message.split(" ");
		        
		        //This is the format urlToCall has to be in order for it to work
		        urlToCall = "http://" + tokens[1].substring(0, tokens[1].length() - 4);
		        //urlToCall = tokens[1]; This is what was here before
		        
		        System.out.println("The URL requested is: " + urlToCall);
		        //Call Logit here to place log into a text file
		        logger.logRequest(urlToCall, clientSocket.getLocalAddress().getLocalHost().toString());
		        
		        // counts how many tokens there are
		        length = tokens.length;
		        
			    //Getting the webpage requested
			    System.out.println("Getting urlToCall...");
			    
			    BufferedReader is = null;
			    
			    try {
			        URL currentUrl = new URL(urlToCall);
			        HttpURLConnection urlConn = (HttpURLConnection) currentUrl.openConnection();
			        urlConn.connect();
			        System.out.println(urlConn.getResponseCode());
			        
			        is = new BufferedReader(
					        new InputStreamReader(urlConn.getInputStream()));
					String inputLine;
					StringBuffer responsebuf = new StringBuffer();

					while ((inputLine = is.readLine()) != null) {
						responsebuf.append(inputLine);
					}
					is.close();
					
					response = responsebuf.toString();
			        
			    } catch (IOException e) {
			        System.err.println("Error: Couldn't resolve http connection.");
			        e.printStackTrace();
			        System.out.println("Couldn't find the specified url!");
			    }
			    
			    System.out.println(response);
			    
			    //Sending the response back to the client.
			    System.out.println("Starting writeback to client...");

			    //Gives an array out of bounds exception,
			    //possibly due to attempting to send an entire
			    //html document in one write method.
			    //This is where I think we seperate it into bytes.
			    PrintWriter out = new PrintWriter(outputStream);
			    out.write(response);
			    
			    System.out.println("Writeback successful!");
			    
			    //Test this by putting "www.google.com" into the url bar in firefox. 
			    //It should return the header of the page and the html.
		    }

		} finally {
			clientSocket.close();
		}
	}
}