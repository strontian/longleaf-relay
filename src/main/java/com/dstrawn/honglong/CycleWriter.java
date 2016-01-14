package com.dstrawn.honglong;

import com.dstrawn.honglong.server.RelayServer;
import java.util.TimerTask;

/**
 *
 * @author dks
 */
public class CycleWriter extends TimerTask {

    RelayServer hsh;
    int cycle;
    
    public CycleWriter(RelayServer hsh) {
        this.hsh = hsh;
        cycle = 0;
    }
    
    @Override
    public void run() {
        hsh.writeCycle(cycle);
        cycle++;
    }
    
}
