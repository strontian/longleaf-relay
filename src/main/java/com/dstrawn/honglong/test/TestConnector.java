package com.dstrawn.honglong.test;

import com.dstrawn.datamsgs.pojos.JSONWrapper;
import com.dstrawn.honglong.HongConfig;
import com.dstrawn.honglong.NettyJSONPublisher;
import org.jboss.netty.channel.MessageEvent;

/**
 *
 * @author dks
 */
public class TestConnector extends NettyJSONPublisher {

    @Override
    public void handleJSON(JSONWrapper jw, MessageEvent e) {
        System.out.println("Got JSON, key:" + jw.key + ", type:" + jw.messageType + ", payload:" + jw.payload);
    }

    public void onConnection() {
        System.out.println("Test connected!");
    }
    
    public static void main(String args[]) {
        TestConnector tc = new TestConnector();
        tc.configAndConnect(HongConfig.host, HongConfig.port);
        tc.subData("MISSING_SYMBOLS");
    }

    public void onFailedConnection() {
        System.out.println("Test failed to connect!");
    }
    
}
