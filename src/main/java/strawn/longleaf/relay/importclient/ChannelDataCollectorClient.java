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

package strawn.longleaf.relay.importclient;

import strawn.longleaf.relay.messages.RelayMessage;
import strawn.longleaf.relay.client.RelayClient;
import strawn.longleaf.relay.exceptions.AlreadySubscribedException;
import strawn.longleaf.relay.exceptions.DataNotFoundException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jboss.netty.channel.MessageEvent;

/**
 *
 * @author David Strawn
 */
public class ChannelDataCollectorClient extends RelayClient {
    
    Map<String, ChannelDataCollection> data;
    Gson g;
    
    public ChannelDataCollectorClient() {
        g = new Gson();
        data = new HashMap();
    }
    
    public <T> List<T> getValues(String key, Class<T> clazz) throws DataNotFoundException {
        ArrayList<T> ret = new ArrayList();
        ChannelDataCollection hc = data.get(key);
        if(hc == null) {
            throw new DataNotFoundException(key);
        }
        for(String s : hc.values) {
            ret.add(g.fromJson(s, clazz));
        }
        return ret;
    }
    
    @Override
    public void handleMessage(RelayMessage jw, MessageEvent e) {
        ChannelDataCollection hc = data.get(jw.channelName);
        if(jw.messageType.equals("END_REFRESH")) {
            hc.completed = true;
        }else {
            hc.addData(jw.payload);
        }
    }
    
    public void onConnection() {
        
    }

    public void onFailedConnection() {
        
    }
    
    public void collectStream(String channelName) throws AlreadySubscribedException {
        ChannelDataCollection hc = data.get(channelName);
        if(hc != null) {
            throw new AlreadySubscribedException(channelName);
        }
        hc = new ChannelDataCollection(channelName);
        data.put(channelName, hc);
        subscribeChannel(channelName);
    }
    
    public boolean collectionComplete() {
        for(ChannelDataCollection hc : data.values()) {
            if(!hc.isCompleted()) {
                return false;
            }
        }
        return true;
    }
    
}
