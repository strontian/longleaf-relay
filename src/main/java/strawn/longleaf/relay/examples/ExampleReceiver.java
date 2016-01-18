package strawn.longleaf.relay.examples;

import java.io.IOException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import strawn.longleaf.relay.client.RelayClient;
import org.jboss.netty.channel.MessageEvent;
import strawn.longleaf.relay.messages.RelayMessage;
import strawn.longleaf.relay.util.RelayConfigLoader;

/**
 *
 * @author David Strawn
 * 
 * This example client connects to the server on channel 'example' and prints to System.out when
 * it receives a message.
 * 
 */
public class ExampleReceiver extends RelayClient {

    public static void main(String[] args) {
        RelayConfigLoader configLoader = new RelayConfigLoader();
        ExampleReceiver exampleReceiver = new ExampleReceiver();
        try {
            configLoader.loadClientConfig();
            exampleReceiver.configAndConnect(configLoader.getHost(), configLoader.getPort());
        } catch (IOException ex) {
            System.out.println("Exception starting example receiver:" + ex.getMessage());
        }
    }
    
    @Override
    public void handleMessage(RelayMessage relayMessage, MessageEvent e) {
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
