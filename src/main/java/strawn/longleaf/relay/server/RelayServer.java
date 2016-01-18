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
    
    protected Map<String, Set<Channel>> subscribers;
    protected Map<String, Set<Channel>> mapSubscribers;
    protected Map<String, List<String>> allChannelData;
    protected Map<String, Map<String, String>> allMapChannelData;
    
    protected ServerBootstrap bootstrap;
    
    public RelayServer() {
        subscribers = new HashMap();
        mapSubscribers = new HashMap();
        allChannelData = new HashMap();
        allMapChannelData = new HashMap();
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
        System.out.println("Got Netty exception:" + tos);
        //String res = e.getCause().getMessage();
        //String loc = e.getCause().getLocalizedMessage();
        //System.out.println("got exception:" + res);
        //System.out.println("got exception:" + loc);
    }
    
    @Override
    public void handleMessage(RelayMessage relayMessage, MessageEvent e) {
        System.out.println("Server Got Message:" + (String)e.getMessage());
        switch (relayMessage.messageType) {
            case SUBSCRIBE:
                addSubscriber(e.getChannel(), relayMessage.channelName);
                break;
            case SUBSCRIBE_MAP:
                addMapSubscriber(e.getChannel(), relayMessage.channelName);
                break;
            case DATA:
                cacheAndPublish(relayMessage.channelName, (String)e.getMessage());
                break;
            case FLUSH:
                flushChannel(relayMessage.channelName, (String)e.getMessage());
                break;
            case FLUSH_MAP:
                flushMap(relayMessage.channelName, (String)e.getMessage());
                break;
            case BROADCAST:
                publishData(relayMessage.channelName, (String)e.getMessage());
                break;
            case DATA_MAP:
                replaceData(relayMessage.channelName, relayMessage.messageKey, (String)e.getMessage());
                break;
            case UNSUBSCRIBE:
                removeSubscriber(e.getChannel(), relayMessage.channelName);
                break;
            case UNSUBSCRIBE_MAP:
                removeMapSubscriber(e.getChannel(), relayMessage.channelName);
                break;
        }
    }
    
    protected void replaceData(String key, String messageKey, String data) {
        Map<String, String> mapChannelData = allMapChannelData.get(key);
        if(mapChannelData == null) {
            mapChannelData = new HashMap();
            allMapChannelData.put(key, mapChannelData);
        }
        mapChannelData.put(messageKey, data);
        publishData(key, data);
    }
    
    protected void flushChannel(String channelKey, String flushMessage) {
        Set<Channel> channelGroup = subscribers.get(channelKey);
        for (Channel c : channelGroup) {
            c.write(flushMessage + "\n");
        }
        //subscribers.remove(key);
        allChannelData.remove(channelKey);
    }
    
    protected void flushMap(String channelKey, String flushMessage) {
        Set<Channel> channelGroup = mapSubscribers.get(channelKey);
        for(Channel c : channelGroup) {
            c.write(flushMessage + "\n");
        }
        //mapSubscribers.remove(key);
        allMapChannelData.remove(channelKey);
    }
    
    protected void publishData(String channelKey, String datum) {
        Set<Channel> group = subscribers.get(channelKey);
        if(group != null) {
            Iterator<Channel> i = group.iterator();
            while(i.hasNext()) {
                Channel c = i.next();
                if(c.isConnected()) {
                    //the channel is connected, write the message
                    //System.out.println("Found connected Channel, routing data:" + datum);
                    c.write(datum + "\n");
                }else {
                    //if the channel is disconnected, remove it from the subscriber group
                    //System.out.println("Can't route data, channel null - removing channel from subscriber set.");
                    i.remove();
                }
            }
        }
    }
    
    protected void removeSubscriber(Channel toRemove, String channelKey) {
        Set<Channel> group = subscribers.get(channelKey);
        if(group != null) {
            Iterator<Channel> i = group.iterator();
            while(i.hasNext()) {
                Channel channel = i.next();
                if(channel.equals(toRemove)) {
                    i.remove();
                }
            }
        }
    }
    
    protected void removeMapSubscriber(Channel toRemove, String channelKey) {
        Set<Channel> group = mapSubscribers.get(channelKey);
        if(group != null) {
            Iterator<Channel> i = group.iterator();
            while(i.hasNext()) {
                Channel channel = i.next();
                if(channel.equals(toRemove)) {
                    i.remove();
                }
            }
        }
    }
    
    protected void addSubscriber(Channel c, String channelKey) {
        
        //add channel to the subscriber group
        Set<Channel> group = subscribers.get(channelKey);
        if(group == null) {
            group = new HashSet();
            subscribers.put(channelKey, group);
        }
        group.add(c);
        
        //forward all cached messages to new subscriber
        List<String> data = allChannelData.get(channelKey);
        if(data != null) {
            for(String s : data) {
                c.write(s + "\n");
            }
        }
        //write END_REFRESH message to new subscriber once all data is sent
        c.write(getEndRefreshString(channelKey));
    }
    
    protected void addMapSubscriber(Channel c, String key) {
        
        //add channel to the subscriber group
        Set<Channel> group = mapSubscribers.get(key);
        if(group == null) {
            //create subscriber group
            group = new HashSet();
            mapSubscribers.put(key, group);
        }
        group.add(c);
        
        //forward all cached messages to new subscriber
        Map<String, String> dataMap = allMapChannelData.get(key);
        if(dataMap != null) {
            for(String s : dataMap.values()) {
                c.write(s + "\n");
            }
        }
        //write END_REFRESH message to new subscriber once all data is sent
        c.write(getEndRefreshString(key));
    }
    
    protected String getEndRefreshString(String key) {
        RelayMessage jw = getEndRefreshMessage(key);
        String s = g.toJson(jw);
        return s + "\n";
    }

    protected static RelayMessage getEndRefreshMessage(String key) {
        RelayMessage jw = new RelayMessage();
        jw.channelName = key;
        jw.messageType = RelayMessageType.END_REFRESH;
        return jw;
    }
    
    protected void cacheAndPublish(String key, String datum) {
        publishData(key, datum);
        List<String> data = allChannelData.get(key);
        if(data == null) {
            data = new ArrayList();
            allChannelData.put(key, data);
        }
        data.add(datum);
    }
    
}
