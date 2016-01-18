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
public abstract class RelayMessageHandler extends SimpleChannelUpstreamHandler {
    
    protected Gson gson;
    
    /**
     * 
     * This method should be overridden on the client to handle RelayMessages
     * 
     * @param relayMessage The message received by the client
     * @param e The MessageEvent from Netty
     */
    public abstract void handleMessage(RelayMessage relayMessage, MessageEvent e);
    
    public RelayMessageHandler() {
        gson = new Gson();
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String s = (String)e.getMessage();
        handleMessage(gson.fromJson(s, RelayMessage.class), e);
    }
    
}
