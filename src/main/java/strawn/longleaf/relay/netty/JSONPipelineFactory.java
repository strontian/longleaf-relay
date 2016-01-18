/**
 * 
 * Copyright 2016 David Strawn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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

    protected RelayMessageHandler jh;
    
    public JSONPipelineFactory(RelayMessageHandler jh) {
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
