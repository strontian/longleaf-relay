package strawn.longleaf.relay.importclient;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Strawn
 */
public class ChannelDataCollection {
    
    public String key;
    public boolean completed;
    public List<String> values;
    
    public ChannelDataCollection(String key) {
        values = new ArrayList();
        completed = false;
        this.key = key;
    }
    
    public void addData(String data) {
        values.add(data);
    }
    
    public void complete() {
        completed = true;
    }
    
    public boolean isCompleted(){ 
        return completed;
    }
    
}
