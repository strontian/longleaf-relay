package strawn.longleaf.relay.examples;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import strawn.longleaf.relay.client.RelayJSONClient;
import org.jboss.netty.channel.MessageEvent;
import strawn.longleaf.relay.messages.RelayMessage;
import strawn.longleaf.relay.util.RelayConfigLoader;

/**
 *
 * @author David Strawn
 */
public class TestConnector extends RelayJSONClient {

    @Override
    public void handleJSON(RelayMessage rm, MessageEvent e) {
        System.out.println("Got JSON, channel:" + rm.channelKey + ", type:" + rm.messageType + ", payload:" + rm.payload);
    }

    public void onConnection() {
        System.out.println("Test connected!");
    }
    
    public static void main(String args[]) {
        TestConnector tc = new TestConnector();
        RelayConfigLoader configLoader = new RelayConfigLoader();
        try {
            configLoader.loadClientConfig();
        } catch (IOException ex) {
            System.out.println("Could not load config file:" + ex.getMessage());
            return;
        }
        tc.configAndConnect(configLoader.getHost(), configLoader.getPort());
        tc.subData("MISSING_SYMBOLS");
    }

    public void onFailedConnection() {
        System.out.println("Test failed to connect!");
    }
    
}
