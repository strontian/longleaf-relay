package strawn.longleaf.relay.tasks;

import strawn.longleaf.relay.server.RelayServer;
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
