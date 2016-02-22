/* This class is designed to log user requests when called for each ProxyThread.
 * Logs: Time-Stamp, Requested URL, User IP, and url size in bytes.
 * Created for Assignment 2 - 415 Network and Parallel Computation
 * 02.22.16
 */
package proxy;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

/**
 *
 * @author Matthew
 */
public class LogIt 
{
    public LogIt()
    {
    }
    
    String logFile = "logRequests.txt";
    //SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:ms");
    Date date = new Date();
    
    // the keyword synchronized is used to ensure threads are not overwriting a critical area. i.e. the logRequests.txt file
    public synchronized void logRequest(String clientURL, String clientIP, int urlSizeByte) throws IOException 
    {
        
        try
        {
        	//prints to txt file
            PrintWriter outputStream = new PrintWriter(new FileWriter(logFile, true));
            outputStream.write("\r\n" + date + "	" + clientURL + "	" + clientIP + "	" + urlSizeByte);
            
            
            outputStream.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.print("Error writing to log file");
            e.printStackTrace();
        }
                
                
        
    }
    
    
}
