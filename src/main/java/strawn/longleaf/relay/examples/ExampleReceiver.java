package strawn.longleaf.relay.examples;

import strawn.longleaf.relay.client.RelayJSONClient;
import org.jboss.netty.channel.MessageEvent;
import strawn.longleaf.relay.messages.RelayMessage;

/**
 *
 * @author David Strawn
 * 
 * This example client connects to the server on channel 'example' and prints to System.out when
 * it receives a message.
 * 
 */
public class ExampleReceiver extends RelayJSONClient {

    @Override
    public void handleJSON(RelayMessage relayMessage, MessageEvent e) {
        System.out.println("Got JSON, channel:" + relayMessage.channelName + ", type:" + relayMessage.messageType + ", payload:" + relayMessage.payload);
    }

    public void onConnection() {
        System.out.println("ExampleReceiver receiver connected!");
        System.out.println("Subscribing to channel:'example'");
        subscribeToChannel("example");
    }

    public void onFailedConnection() {
        System.out.println("ExampleReceiver failed to connect!");
    }
    
}
