package proxy;

import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyThread extends Thread {
    private Socket socket = null;
    SimpleCache webCache;
    boolean pageIsCached = false;
    private static final int BUFFER_SIZE = 32768;
    public ProxyThread(Socket socket, SimpleCache cache) {
        super("ProxyThread");
        this.socket = socket;
        this.webCache = cache;
    }
    // for logging user requests
    LogIt logger = new LogIt();
    int urlSizeByte;
    

    public void run() {
        //get input from user
        //send request to server
        //get response from server
        //send response to user
        

        try {
            
            
            DataOutputStream out =
		new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(
		new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;
            int cnt = 0;
            String urlToCall = "";
            ///////////////////////////////////
            //begin get request from client
            while ((inputLine = in.readLine()) != null) {
                try {
                    StringTokenizer tok = new StringTokenizer(inputLine);
                    tok.nextToken();
                } catch (Exception e) {
                    break;
                }
                //parse the first line of the request to find the url
                if (cnt == 0) {
                    String[] tokens = inputLine.split(" ");
                    urlToCall = tokens[1];
                    //can redirect this to output log
                    System.out.println("Request for : " + urlToCall);
                }

                cnt++;
            }
            //end get request from client
            ///////////////////////////////////
            
            if(webCache.get(urlToCall) != null)
            {
                System.out.println("Web page " + urlToCall + " found in cache.");
                pageIsCached = true;
            }
            else
            {
                System.out.println("Web page " + urlToCall + " not found in cache.");
                pageIsCached = false;
            }
            
            ///////////////////////////////////


            BufferedReader rd = null;
            try {
                
                ///////////////////////////////////
                //begin send request to server, get response from server
                URL url = new URL(urlToCall);
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                //not doing HTTP posts
                conn.setDoOutput(false);

                // Get the response
                InputStream is = null;
                HttpURLConnection huc = (HttpURLConnection)conn;
                if (conn.getContentLength() > 0) {
                    try {
                        is = conn.getInputStream();
                        rd = new BufferedReader(new InputStreamReader(is));
                    } catch (IOException ioe) {
                        System.out.println(
				"********* IO EXCEPTION **********: " + ioe);
                    }
                }
                
                
                
                //Update the webCache
                webCache.put(urlToCall, is);
                //end send request to server, get response from server
                ///////////////////////////////////

                ///////////////////////////////////
                //begin send response to client
                InputStream is2 = (InputStream) webCache.get(urlToCall);
                
                byte by[] = new byte[ BUFFER_SIZE ];
                int index = is2.read( by, 0, BUFFER_SIZE );
                urlSizeByte = index; // used by LogIt class to output the size of the url to log
                while ( index != -1 )
                {
                  out.write( by, 0, index );
                  index = is2.read( by, 0, BUFFER_SIZE );
                }
                // calls the LogIt log function to store request info into logRequests.txt
                logger.logRequest(urlToCall, socket.getLocalAddress().getHostAddress().toString(), urlSizeByte);
                out.flush();

                //end send response to client
                ///////////////////////////////////
            } catch (Exception e) {
                //can redirect this to error log
                System.err.println("Encountered exception: " + e);
                //encountered error - just send nothing back, so
                //processing can continue
                out.writeBytes("");
            }

            //close out all resources
            if (rd != null) {
                rd.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}