package strawn.longleaf.relay.examples;

import strawn.longleaf.relay.HongConfig;
import strawn.longleaf.relay.client.NettyJSONPublisher;
import org.jboss.netty.channel.MessageEvent;
import strawn.longleaf.relay.messages.RelayMessage;

/**
 *
 * @author dks
 */
public class TestConnector extends NettyJSONPublisher {

    @Override
    public void handleJSON(RelayMessage rm, MessageEvent e) {
        System.out.println("Got JSON, key:" + rm.key + ", type:" + rm.messageType + ", payload:" + rm.payload);
    }

    public void onConnection() {
        System.out.println("Test connected!");
    }
    
    public static void main(String args[]) {
        TestConnector tc = new TestConnector();
        tc.configAndConnect(HongConfig.host, HongConfig.port);
        tc.subData("MISSING_SYMBOLS");
    }

    public void onFailedConnection() {
        System.out.println("Test failed to connect!");
    }
    
}
