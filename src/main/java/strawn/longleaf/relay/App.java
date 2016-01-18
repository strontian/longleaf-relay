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

package strawn.longleaf.relay;

import java.io.IOException;
import strawn.longleaf.relay.server.RelayServer;
import strawn.longleaf.relay.util.RelayConfigLoader;

/**
 * 
 * @author David Strawn
 * 
 * This is the entry point for the server. With no arguments or when run with argument 'server',
 * it launches the relay server.
 * 
 * It also accepts arguments: sender, receiver
 * 
 * -When run with argument 'sender', it creates an example client that sends data to the server.
 * -When run with argument 'receiver', it creates an example client that connects to the server and listens for data.
 * 
 */
public class App {
    
    public static void main(String[] args) {
            
        RelayConfigLoader configLoader = new RelayConfigLoader();
        RelayServer relayServer = new RelayServer();
        
        try {
            configLoader.loadServerConfig();
            relayServer.connect(configLoader.getPort());
            System.out.println("Started server on port:" + configLoader.getPort());
        } catch (IOException ex) {
            System.out.println("Exception starting server:" + ex.getMessage());
        }
        
    }
    
}
