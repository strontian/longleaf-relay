package strawn.longleaf.relay.examples.tasks;

import strawn.longleaf.relay.client.Connector;
import java.util.TimerTask;

/**
 *
 * @author David Strawn
 * 
 */
public class ClientReconnector extends TimerTask {

    protected Connector c;
    
    public ClientReconnector(Connector c) {
        this.c = c;
    }
    
    @Override
    public void run() {
        if(!c.isConnected()) {
            boolean status = c.reconnect();
            if(status) {
                System.out.println("Connection succeeded, subbing data");
                c.onConnection();
            }else {
                c.onFailedConnection();
            }
        }
    }
    
}
