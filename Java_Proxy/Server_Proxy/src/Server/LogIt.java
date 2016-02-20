/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
    
    public synchronized void logRequest(String clientURL, String clientIP) throws IOException 
    {
        
        try
        {
            PrintWriter outputStream = new PrintWriter(new FileWriter(logFile, true));
            outputStream.write("\r\n" + "Date: " + date + "  URL: " + clientURL + "  Client IP: " + clientIP);
            
            
            outputStream.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.print("Error writing to log file");
            e.printStackTrace();
        }
                
                
        
    }
    
    
}
