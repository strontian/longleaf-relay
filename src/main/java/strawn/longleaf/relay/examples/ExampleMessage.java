package strawn.longleaf.relay.examples;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;

/**
 * This is a POJO, used to demonstrate sending an object as JSON in the examples
 * 
 * @author David Strawn
 */
public class ExampleMessage {
    
    public String value1;
    public String value2;
    
    public ExampleMessage(String value1, String value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
    
}
