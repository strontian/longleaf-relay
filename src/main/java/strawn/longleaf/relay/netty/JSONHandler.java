package strawn.longleaf.relay.netty;

import com.google.gson.Gson;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import strawn.longleaf.relay.messages.RelayMessage;

/**
 *
 * @author David Strawn
 * 
 * This is a decoder to be used in the Netty pipeline. When a message is received from the network,
 * a handler that is previous to this one interprets it as a String, and this handler converts
 * that String into a RelayMessage object that the clients and server understand.
 */
public abstract class JSONHandler extends SimpleChannelUpstreamHandler {
    
    protected Gson g;
    
    /**
     * 
     * This method should be overridden on the client to describe how it should handle received messages
     * 
     * @param relayMessage The message received by the client
     * @param e The MessageEvent from Netty
     */
    public abstract void handleJSON(RelayMessage relayMessage, MessageEvent e);
    
    public JSONHandler() {
        g = new Gson();
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String s = (String)e.getMessage();
        handleJSON(g.fromJson(s, RelayMessage.class), e);
    }
    
}
