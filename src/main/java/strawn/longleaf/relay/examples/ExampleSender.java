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

package strawn.longleaf.relay.examples;

import java.io.IOException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import strawn.longleaf.relay.client.RelayClient;
import strawn.longleaf.relay.messages.RelayMessage;
import strawn.longleaf.relay.util.RelayConfigLoader;

/**
 *
 * @author David Strawn
 * 
 * This example client connects to the server, and immediately writes two messages to channel 'example'
 * 
 */
public class ExampleSender extends RelayClient {
    
    public static void main(String[] args) {
        ExampleSender exampleSender = new ExampleSender();
        RelayConfigLoader configLoader = new RelayConfigLoader();
        try {
            configLoader.loadClientConfig();
            exampleSender.configAndConnect(configLoader.getHost(), configLoader.getPort());
        } catch (IOException ex) {
            System.out.println("Exception starting example sender:" + ex.getMessage());
        }
    }
    
    public void connect() throws IOException {
        RelayConfigLoader configLoader = new RelayConfigLoader();
        configLoader.loadClientConfig();
        this.configAndConnect(configLoader.getHost(), configLoader.getPort());
    }
    
    @Override
    public void handleMessage(RelayMessage jw, MessageEvent e) {
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
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        String tos = e.getCause().toString();
        String res = e.getCause().getMessage();
        String loc = e.getCause().getLocalizedMessage();
        System.out.println("got exception:" + tos);
        System.out.println("got exception:" + res);
        System.out.println("got exception:" + loc);
    }
    
}
