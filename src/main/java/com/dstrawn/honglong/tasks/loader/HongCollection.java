package com.dstrawn.honglong.tasks.loader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class HongCollection {
    
    public String key;
    public boolean completed;
    public List<String> values;
    
    public HongCollection(String key) {
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
