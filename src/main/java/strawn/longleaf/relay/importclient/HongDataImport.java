package strawn.longleaf.relay.importclient;

import com.dstrawn.datamsgs.pojos.JSONWrapper;
import strawn.longleaf.relay.client.NettyJSONPublisher;
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
public class HongDataImport extends NettyJSONPublisher {
    
    Map<String, HongCollection> data;
    Gson g;
    
    public HongDataImport() {
        g = new Gson();
        data = new HashMap();
    }
    
    public <T> List<T> getValues(String key, Class<T> clazz) throws DataNotFoundException {
        ArrayList<T> ret = new ArrayList();
        HongCollection hc = data.get(key);
        if(hc == null) {
            throw new DataNotFoundException(key);
        }
        for(String s : hc.values) {
            ret.add(g.fromJson(s, clazz));
        }
        return ret;
    }
    
    @Override
    public void handleJSON(JSONWrapper jw, MessageEvent e) {
        HongCollection hc = data.get(jw.key);
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
        HongCollection hc = data.get(channelName);
        if(hc != null) {
            throw new AlreadySubscribedException();
        }
        hc = new HongCollection(channelName);
        data.put(channelName, hc);
        subData(channelName);
    }
    
    public boolean collectionComplete() {
        for(HongCollection hc : data.values()) {
            if(!hc.isCompleted()) {
                return false;
            }
        }
        return true;
    }
    
}
