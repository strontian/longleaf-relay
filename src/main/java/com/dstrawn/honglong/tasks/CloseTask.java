package com.dstrawn.honglong.tasks;

import com.dstrawn.honglong.Connector;
import java.util.TimerTask;

/**
 *
 * @author david
 */
public class CloseTask extends TimerTask {

    protected Connector c;
    
    public CloseTask(Connector c) {
        this.c = c;
    }
    
    @Override
    public void run() {
        c.close();
    }
    
}
