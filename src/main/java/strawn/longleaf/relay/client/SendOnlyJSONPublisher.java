package strawn.longleaf.relay.client;

import org.jboss.netty.channel.MessageEvent;
import strawn.longleaf.relay.messages.RelayMessage;

/**
 *
 * @author David Strawn
 */
public class SendOnlyJSONPublisher extends RelayJSONClient {

    @Override
    public void handleJSON(RelayMessage rm, MessageEvent e) {
        
    }

    public void onConnection() {
        
    }

    public void onFailedConnection() {
        
    }

}
