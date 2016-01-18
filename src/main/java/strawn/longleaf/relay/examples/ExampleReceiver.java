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
