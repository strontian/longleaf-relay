package strawn.longleaf.relay.netty;

import com.google.gson.Gson;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import strawn.longleaf.relay.messages.RelayMessage;

/**
 *
 * @author David Strawn
 */
public abstract class JSONHandler extends SimpleChannelUpstreamHandler {
    
    public abstract void handleJSON(RelayMessage jw, MessageEvent e);
    
    protected Gson g;
    
    public JSONHandler() {
        g = new Gson();
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String s = (String)e.getMessage();
        handleJSON(g.fromJson(s, RelayMessage.class), e);
    }
    
}
