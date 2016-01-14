package com.dstrawn.honglong.tasks;

import com.dstrawn.honglong.server.RelayServer;
import java.util.TimerTask;

/**
 *
 * @author david
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
