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

package strawn.longleaf.relay.client;

/**
 *
 * @author David Strawn
 * 
 * This interface is implemented by clients that connect to the relay server
 * 
 */
public interface Connector {
    
    public abstract boolean reconnect();
    public abstract boolean isConnected();
    public abstract void onConnection();
    public abstract void onFailedConnection();
    public abstract void disconnect();
    public abstract void configAndConnect(String address, int port);
    
}
