package strawn.longleaf.relay.tasks;

import strawn.longleaf.relay.server.RelayServer;
import java.util.TimerTask;

/**
 *
 * @author dks
 */
public class TimeNotificationTask extends TimerTask {

    protected RelayServer hsh;
    
    public TimeNotificationTask(RelayServer hsh) {
        this.hsh = hsh;
    }
            
    @Override
    public void run() {
        hsh.writeTime();
    }
    
}
