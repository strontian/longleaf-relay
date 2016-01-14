package strawn.longleaf.relay.tasks;

import strawn.longleaf.relay.server.RelayServer;
import java.util.TimerTask;

/**
 *
 * @author David Strawn
 */
public class StopCycleNotificationTask extends TimerTask {
    
    protected RelayServer hsh;
    
    public StopCycleNotificationTask(RelayServer hsh) {
        this.hsh = hsh;
    }
            
    @Override
    public void run() {
        hsh.writeStopSycle();
    }
    
    
}
