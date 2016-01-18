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

package strawn.longleaf.relay.examples.tasks;

import strawn.longleaf.relay.client.Connector;
import java.util.TimerTask;

/**
 *
 * @author David Strawn
 * 
 */
public class ClientReconnector extends TimerTask {

    protected Connector c;
    
    public ClientReconnector(Connector c) {
        this.c = c;
    }
    
    @Override
    public void run() {
        if(!c.isConnected()) {
            boolean status = c.reconnect();
            if(status) {
                System.out.println("Connection succeeded, subbing data");
                c.onConnection();
            }else {
                c.onFailedConnection();
            }
        }
    }
    
}
