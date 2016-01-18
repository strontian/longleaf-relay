package strawn.longleaf.relay.server;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import strawn.longleaf.relay.messages.RelayMessage;
import strawn.longleaf.relay.messages.RelayMessageType;
import strawn.longleaf.relay.netty.RelayMessageHandler;
import strawn.longleaf.relay.netty.JSONPipelineFactory;

/**
 *
 * RelayServer is a simple JSON storage and reflection server.
 * 
 * @author David Strawn
 */
public class RelayServer extends RelayMessageHandler {
    
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
    public void handleMessage(RelayMessage jw, MessageEvent e) {
        System.out.println("Server Got Message:" + (String)e.getMessage());
        switch (jw.messageType) {
            case SUBSCRIBE:
                addSubscriber(e.getChannel(), jw.channelName);
                break;
            case DATA:
                cacheAndPublish(jw.channelName, (String)e.getMessage());
                break;
            case FLUSH:
                flushStream(jw.channelName);
                break;
            case BROADCAST:
                publishData(jw.channelName, (String)e.getMessage());
                break;
            case REPLACE:
                replaceData(jw.channelName, jw.messageKey, (String)e.getMessage());
                break;
        }
        
    }
    
    protected void replaceData(String key, String messageKey, String data) {
        Map<String, String> channelData = replaceData.get(key);
        if(channelData == null) {
            channelData = new HashMap();
            replaceData.put(key, channelData);
        }
        channelData.put(messageKey, data);
        publishData(key, data);
    }
    
    protected void flushStream(String key) {
        subs.remove(key);
        datasets.remove(key);
        //what should the behavior be for existing listeners of flushed data?
    }
    
    protected void publishData(String key, String datum) {
        Set<Channel> group = subs.get(key);
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
    
    protected void addSubscriber(Channel c, String key) {
        Set<Channel> group = subs.get(key);
        if(group == null) {
            //System.out.println("Adding sub group:" + key);
            group = new HashSet();
            subs.put(key, group);
        }
        group.add(c);
        List<String> data = datasets.get(key);
        if(data != null) {
            for(String s : data) {
                //System.out.println("Found data, spooling:" + s);
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
        c.write(getEndRefreshString(key));
    }
    
    protected String getEndRefreshString(String key) {
        RelayMessage jw = getEndRefreshMessage(key);
        String s = g.toJson(jw);
        return s + "\n";
    }

    protected RelayMessage getEndRefreshMessage(String key) {
        RelayMessage jw = new RelayMessage();
        jw.channelName = key;
        jw.messageType = RelayMessageType.END_REFRESH;
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
    
}
