package strawn.longleaf.relay.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 *
 * @author David Strawn
 * 
 * This is a class required by Netty, it puts a JSON Handler in Netty's pipeline, 
 * along with string encoder/decoders. When a message comes off the network, this tells
 * the pipeline to interpret the data as a string, and then to interpret the string as
 * a JSON object.
 * 
 */
public class JSONPipelineFactory implements ChannelPipelineFactory {

    protected JSONHandler jh;
    
    public JSONPipelineFactory(JSONHandler jh) {
        this.jh = jh;
    }
    
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline p = Channels.pipeline();
        p.addLast("frameDecoder", new DelimiterBasedFrameDecoder(10000, Delimiters.lineDelimiter()));
        p.addLast("stringDecoder", new StringDecoder());
        p.addLast("stringEncoder", new StringEncoder());
        p.addLast("handler", jh);
        return p;
    }
    
}
