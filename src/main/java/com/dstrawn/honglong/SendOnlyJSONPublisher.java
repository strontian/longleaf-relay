package com.dstrawn.honglong;

import com.dstrawn.datamsgs.pojos.JSONWrapper;
import org.jboss.netty.channel.MessageEvent;

/**
 *
 * @author david
 */
public class SendOnlyJSONPublisher extends NettyJSONPublisher {

    @Override
    public void handleJSON(JSONWrapper jw, MessageEvent e) {
        
    }

    public void onConnection() {
        
    }

    public void onFailedConnection() {
        
    }

}
