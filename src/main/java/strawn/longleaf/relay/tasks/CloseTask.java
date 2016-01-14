package strawn.longleaf.relay.tasks;

import strawn.longleaf.relay.client.Connector;
import java.util.TimerTask;

/**
 *
 * @author David Strawn
 */
public class CloseTask extends TimerTask {

    protected Connector c;
    
    public CloseTask(Connector c) {
        this.c = c;
    }
    
    @Override
    public void run() {
        c.close();
    }
    
}
