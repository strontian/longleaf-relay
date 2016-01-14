package com.dstrawn.honglong.server;

import com.dstrawn.datamsgs.netty.json.JSONHandler;
import com.dstrawn.datamsgs.netty.json.JSONPipelineFactory;
import com.dstrawn.datamsgs.pojos.JSONCommand;
import com.dstrawn.datamsgs.pojos.JSONWrapper;
import com.dstrawn.datamsgs.utils.TimeTools;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * HongServer is a simple JSON storage and reflection server
 * 
 * @author david
 */
public class RelayServer extends JSONHandler {
    
    protected Map<String, Set<Channel>> subs;
    protected Map<String, List<String>> datasets;
    protected Map<String, Map<String, String>> replaceData;
    
    protected ServerBootstrap bootstrap;
    
    public RelayServer() {
        subs = new HashMap();
        datasets = new HashMap();
        replaceData = new HashMap();
    }
    
    public void connect(int port) {
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), 1));
        bootstrap.setPipelineFactory(new JSONPipelineFactory(this));
        bootstrap.setOption("tcpNoDelay", "true");
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(port));
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        String tos = e.getCause().toString();
        String res = e.getCause().getMessage();
        String loc = e.getCause().getLocalizedMessage();
        System.out.println("got exception:" + tos);
        System.out.println("got exception:" + res);
        System.out.println("got exception:" + loc);
    }
    
    @Override
    public void handleJSON(JSONWrapper jw, MessageEvent e) {
        //System.out.println("Got message:" + jw.messageType + ", " + jw.key + ", " + jw.payload);
        if(jw.messageType.equals("SUBSCRIBE")) {
            addSub(e.getChannel(), jw.key);
        }else if(jw.messageType.equals("DATA")) {
            cacheAndPublish(jw.key, (String)e.getMessage());
        }else if(jw.messageType.equals("END_DATA")) {
            cacheAndPublish(jw.key, (String)e.getMessage());
        }else if(jw.messageType.equals("FLUSH")) {
            flushStream(jw.key);
        }else if(jw.messageType.equals("PUBLISH")) {
            publishData(jw.key, (String)e.getMessage());
        }else if(jw.messageType.equals("REPLACE")) {
            replaceData(jw.key, jw.replaceKey, (String)e.getMessage());
        }
    }
    
    protected void replaceData(String key, String replaceKey, String data) {
        Map<String, String> channelData = replaceData.get(key);
        if(channelData == null) {
            channelData = new HashMap();
            replaceData.put(key, channelData);
        }
        channelData.put(replaceKey, data);
        publishData(key, data);
    }
    
    protected void flushStream(String key) {
        subs.remove(key);
        datasets.remove(key);
        //what should the behavior be for existing listeners of flushed data?
    }
    
    protected void publishData(String key, String datum) {
        Set<Channel> group = subs.get(key);
        //TODO: Replace this with an iterator, so we can remove disconnected Channels...
        if(group != null) {
            Iterator<Channel> i = group.iterator();
            while(i.hasNext()) {
                Channel c = i.next();
                if(c.isConnected()) {
                    System.out.println("Found connected Channel, routing data:" + datum);
                    c.write(datum + "\n");
                }else {
                    i.remove();
                    System.out.println("Can't route data, channel null - removing channel from subscriber set.");
                }
            }
        }
    }
    
    protected void addSub(Channel c, String key) {
        Set<Channel> group = subs.get(key);
        if(group == null) {
            System.out.println("Adding sub group:" + key);
            group = new HashSet();
            subs.put(key, group);
        }
        group.add(c);
        List<String> data = datasets.get(key);
        if(data != null) {
            for(String s : data) {
                System.out.println("Found data, spooling:" + s);
                c.write(s + "\n");
            }
            
        }else {
            Map<String, String> datum = replaceData.get(key);
            if(datum != null) {
                for(String s : datum.values()) {
                    c.write(s + "\n");
                }
            }
            //System.out.println("Data was null, can't spool");
        }
        c.write(getEndString(key));
    }
    
    protected String getEndString(String key) {
        JSONWrapper jw = getEnd(key);
        String s = g.toJson(jw);
        return s + "\n";
    }

    protected JSONWrapper getEnd(String key) {
        JSONWrapper jw = new JSONWrapper();
        jw.key = key;
        jw.messageType = "END_REFRESH";
        return jw;
    }
    
    protected void cacheAndPublish(String key, String datum) {
        publishData(key, datum);
        List<String> data = datasets.get(key);
        if(data == null) {
            data = new ArrayList();
            datasets.put(key, data);
        }
        data.add(datum);
    }
    
    
    public void writeCycle(int index) {
        
        JSONWrapper jw = new JSONWrapper();
        JSONCommand jc = new JSONCommand();
        
        jc.commandName = "CYCLE";
        jc.destSymbol = "BROADCAST";
        jc.int1 = index;
        
        jw.key = "COMMAND";
        jw.payload = g.toJson(jc);
        jw.messageType = "PUBLISH";
        
        String s = g.toJson(jw);
        publishData("CYCLES", s);
        
    }
    
    public void writeTime() {
        
        
        JSONWrapper jw = new JSONWrapper();
        JSONCommand jc = new JSONCommand();
        
        long time = System.currentTimeMillis();
        
        jc.commandName = "NOTIFY_TIME";
        jc.destSymbol = "BROADCAST";
        
        jc.int1 = TimeTools.epochToMillisUntilClose(time, TimeTools.getMidnightMillisInEpochSLOW(TimeTools.getCurrentDate()));
        
        System.out.println("Writing time:" + jc.int1);
        
        jw.messageType = "PUBLISH";
        jw.key = "TIME_NOTES";
        jw.payload = g.toJson(jc);
        
        String s = g.toJson(jw);
        publishData("TIME_NOTES", s);
        
    }

    public void writeStopSycle() {
        JSONWrapper jw = new JSONWrapper();
        JSONCommand jc = new JSONCommand();
        
        long time = System.currentTimeMillis();
        
        jc.commandName = "STOP_CYCLE";
        jc.destSymbol = "BROADCAST";
        
        
        System.out.println("Writing stop_cycle.");
        
        jw.messageType = "PUBLISH";
        jw.key = "COMMAND";
        jw.payload = g.toJson(jc);
        
        String s = g.toJson(jw);
        publishData("CYCLES", s);
        
    }
    
    
}
