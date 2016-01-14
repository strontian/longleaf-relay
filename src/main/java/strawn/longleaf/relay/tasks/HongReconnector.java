package strawn.longleaf.relay.tasks;

import strawn.longleaf.relay.client.Connector;
import java.util.TimerTask;

/**
 *
 * @author David Strawn
 */
public class HongReconnector extends TimerTask {

    protected Connector c;
    
    public HongReconnector(Connector c) {
        this.c = c;
    }
    
    @Override
    public void run() {
        if(!c.isConnected()) {
            //System.out.println("Attempting to connect...");
            boolean status = c.reconnect();
            if(status) {
                System.out.println("Connection succeeded, subbing data");
                c.onConnection();
            }else {
                c.onFailedConnection();
            }
        }else {
            //System.out.println("Client connected, no r/c");
        }
    }
    
}
