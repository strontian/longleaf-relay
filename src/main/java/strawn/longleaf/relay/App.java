package strawn.longleaf.relay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import strawn.longleaf.relay.server.RelayServer;
import strawn.longleaf.relay.tasks.TimeNotificationTask;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 
 */
public class App {
    
    public static void main(String[] args) {
        
        RelayServer relayServer = new RelayServer();
        InputStream inputStream = null;
        
        Properties serverConfig = new Properties();
        try {
            String serverConfigFileName = "serverconfig.properties";
            inputStream = relayServer.getClass().getResourceAsStream(serverConfigFileName);
            if (inputStream != null) {
                serverConfig.load(inputStream);
                int serverPort = Integer.parseInt(serverConfig.getProperty("port"));
                relayServer.connect(serverPort);
            } else {
                throw new FileNotFoundException("server config file:'" + serverConfigFileName + "' not found.");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    System.out.println("Could not close config file InputStream");
                }
            }   
        }
        
        Timer t = new Timer();
        t.schedule(new CycleWriter(hsh), 15000, 90000);
        
        Calendar c = new GregorianCalendar();
        
        c.set(Calendar.HOUR_OF_DAY, 15);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 57);
        c.set(Calendar.MILLISECOND, 0);
        
        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        c.setTimeZone(tz);
        
        Calendar c2 = new GregorianCalendar();
        c2.set(Calendar.MILLISECOND, 0);
        t.schedule(new TimeNotificationTask(hsh), c2.getTime(), 60000);
        
        t.schedule(new TimeNotificationTask(hsh), c.getTime());
        c.set(Calendar.SECOND, 58);
        t.schedule(new TimeNotificationTask(hsh), c.getTime());
        c.set(Calendar.SECOND, 59);
        t.schedule(new TimeNotificationTask(hsh), c.getTime());
        
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 41);
        c.set(Calendar.HOUR_OF_DAY, 15);
        t.schedule(new StopCycleNotificationTask(hsh), c.getTime());
        
    }
    
}
