package strawn.longleaf.relay.examples;

import java.io.IOException;
import org.jboss.netty.channel.MessageEvent;
import strawn.longleaf.relay.client.RelayJSONClient;
import strawn.longleaf.relay.messages.RelayMessage;
import strawn.longleaf.relay.util.RelayConfigLoader;

/**
 *
 * @author David Strawn
 * 
 * This example client connects to the server, and immediately writes two messages to channel 'example'
 * 
 */
public class ExampleSender extends RelayJSONClient {
    
    public void connect() throws IOException {
        RelayConfigLoader configLoader = new RelayConfigLoader();
        configLoader.loadClientConfig();
        this.configAndConnect(configLoader.getHost(), configLoader.getPort());
    }
    
    @Override
    public void handleJSON(RelayMessage jw, MessageEvent e) {
        throw new UnsupportedOperationException("Example Sender received data. It should not subscribe to channels.");
    }

    public void onConnection() {
        System.out.println("Example Sender connected to server.");
        ExampleMessage em = new ExampleMessage("val1", "val2");
        ExampleMessage em2 = new ExampleMessage("val3", "val4");
        publishJSON(em, "example");
        publishJSON(em2, "example");
    }

    public void onFailedConnection() {
        System.out.println("TestSender failed to connect to server.");
    }
}
