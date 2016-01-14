package strawn.longleaf.relay;

import strawn.longleaf.relay.tasks.CycleWriter;
import strawn.longleaf.relay.server.RelayServer;
import strawn.longleaf.relay.tasks.StopCycleNotificationTask;
import strawn.longleaf.relay.tasks.TimeNotificationTask;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;

/**
 * 
 * 
 */
public class App {
    
    public static void main(String[] args) {
        
        Properties config = new Properties();
        
        RelayServer hsh = new RelayServer();
        hsh.connect(HongConfig.port);
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
