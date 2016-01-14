package com.dstrawn.honglong.tasks.loader;

import com.dstrawn.datamsgs.pojos.JSONWrapper;
import com.dstrawn.honglong.NettyJSONPublisher;
import com.dstrawn.honglong.exceptions.AlreadySubscribedException;
import com.dstrawn.honglong.exceptions.DataNotFoundException;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jboss.netty.channel.MessageEvent;

/**
 *
 * @author david
 */
public class HongDataImport extends NettyJSONPublisher {
    
    Map<String, HongCollection> data;
    Gson g;
    
    public HongDataImport() {
        g = new Gson();
        data = new HashMap();
    }
    
    public <T> List<T> getValues(String key, Class<T> t) throws DataNotFoundException {
        ArrayList<T> ret = new ArrayList();
        HongCollection hc = data.get(key);
        if(hc == null) {
            throw new DataNotFoundException();
        }
        for(String s : hc.values) {
            ret.add(g.fromJson(s, t));
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
    
    public void collectStream(String name) throws AlreadySubscribedException {
        HongCollection hc = data.get(name);
        if(hc != null) {
            throw new AlreadySubscribedException();
        }
        hc = new HongCollection(name);
        data.put(name, hc);
        subData(name);
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
