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

package strawn.longleaf.relay.messages;

/**
 *
 * @author David Strawn
 * 
 * All messages sent to the server must be 'wrapped' in this class. 
 */
public class RelayMessage {
    
    public RelayMessageType messageType;
    public String payload;
    public String channelName;
    public String messageKey;
    
    public RelayMessage(RelayMessageType messageType, String payload, String channelName, String messageKey) {
        this.messageType = messageType;
        this.payload = payload;
        this.channelName = channelName;
        this.messageKey = messageKey;
    }

    public RelayMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
