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

package strawn.longleaf.relay.client;

import strawn.longleaf.relay.netty.RelayMessageHandler;
import strawn.longleaf.relay.netty.JSONPipelineFactory;
import strawn.longleaf.relay.messages.RelayMessage;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import strawn.longleaf.relay.messages.RelayMessageType;

/**
 * 
 * @author David Strawn
 * 
 * Clients that want to send data to or receive data from the server should extend this class.
 * 
 */
public abstract class RelayClient extends RelayMessageHandler implements Connector {
    
    protected ClientBootstrap bootstrap;
    protected Channel publishChannel;
    protected String host;
    protected int port;
    
    /**
     * Use this constructor if you would like to provide the threads that the client uses
     * @param boss
     * @param work 
     */
    public RelayClient(Executor boss, Executor work) {
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(boss, work, 1));
        bootstrap.setPipelineFactory(new JSONPipelineFactory(this));
        bootstrap.setOption("tcpNoDelay", "true");
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("keepAlive", true);
    }
    
    public RelayClient() {
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), 1));
        bootstrap.setPipelineFactory(new JSONPipelineFactory(this));
        bootstrap.setOption("tcpNoDelay", "true");
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("keepAlive", true);
    }
    
    /**
     * Attempts to reconnect to the server
     * 
     * @return true if the client was able to reconnect, and false otherwise
     */
    public boolean reconnect() {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        publishChannel = future.awaitUninterruptibly().getChannel(); 
        boolean success = future.isSuccess();
        if(success) {
            onConnection();
        }else {
            onFailedConnection();
        }
        return success;
    }
    
    /**
     * Disconnects from the server
     */
    public void disconnect() {
        publishChannel.close();
        bootstrap.releaseExternalResources();
    }
    
    /**
     * Connects to the server
     * 
     * @param host Hostname of the server
     * @param port Port on which the server is running
     */
    public void configAndConnect(String host, int port) {
        this.host = host;
        this.port = port;
        reconnect();
    }
    
    /**
     * Publishes data as an object, which will be encoded in a JSON string
     * 
     * @param object The object you wish to publish. Stored in the payload field
     * of RelayMessage
     * @param channelName the channel to publish to
     */
    public void publishJSON(Object object, String channelName) {
        String s = g.toJson(object);
        RelayMessage jw = new RelayMessage();
        jw.channelName = channelName;
        jw.messageType = RelayMessageType.DATA;
        jw.payload = s;
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Publishes data as a String
     * 
     * @param toPublish the string to be published
     * @param channelName 
     */
    public void publishString(String toPublish, String channelName) {
        RelayMessage jw = new RelayMessage();
        jw.channelName = channelName;
        jw.messageType = RelayMessageType.DATA;
        jw.payload = toPublish;
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Publishes data as an object to a map channel, which will be encoded in a JSON string
     * 
     * @param object The object you wish to publish. Stored in the payload field
     * of RelayMessage
     * @param messageKey
     * @param channelName the channel to publish to
     */
    public void publishJSONMap(Object object, String messageKey, String channelName) {
        String s = g.toJson(object);
        RelayMessage jw = new RelayMessage();
        jw.channelName = channelName;
        jw.messageType = RelayMessageType.DATA_MAP;
        jw.payload = s;
        jw.messageKey = messageKey;
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Publishes data to a map channel as a String
     * 
     * @param toPublish the string to be published
     * @param messageKey the key of the message
     * @param channelName 
     */
    public void publishStringMap(String toPublish, String messageKey, String channelName) {
        RelayMessage jw = new RelayMessage();
        jw.channelName = channelName;
        jw.messageType = RelayMessageType.DATA_MAP;
        jw.payload = toPublish;
        jw.messageKey = messageKey;
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Publishes data as a String - this data will NOT be cached on the server, and only connected clients
     * will receive it
     * 
     * @param toBroadcast the string to be published
     * @param channelName 
     */
    public void broadcastString(String toBroadcast, String channelName) {
        RelayMessage jw = new RelayMessage();
        jw.channelName = channelName;
        jw.messageType = RelayMessageType.BROADCAST;
        jw.payload = toBroadcast;
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Subscribes to a channel
     * @param channelName - the name of the channel to subscribe
     */
    public void subscribeToChannel(String channelName) {
        RelayMessage relayMessage = new RelayMessage();
        relayMessage.channelName = channelName;
        relayMessage.messageType = RelayMessageType.SUBSCRIBE;
        relayMessage.payload = null;
        String jString = g.toJson(relayMessage);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Subscribes to a map channel
     * @param channelName - the name of the map channel to subscribe
     */
    public void subscribeToMapChannel(String channelName) {
        RelayMessage relayMessage = new RelayMessage();
        relayMessage.channelName = channelName;
        relayMessage.messageType = RelayMessageType.SUBSCRIBE_MAP;
        relayMessage.payload = null;
        String jString = g.toJson(relayMessage);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Unsubscribe from a channel
     * 
     * @param channelName the channel to unsubscribe from
     */
    public void unsubscribeChannel(String channelName) {
        RelayMessage relayMessage = new RelayMessage();
        relayMessage.channelName = channelName;
        relayMessage.messageType = RelayMessageType.UNSUBSCRIBE;
        relayMessage.payload = null;
        String jString = g.toJson(relayMessage);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Unsubscribe from a map channel
     * 
     * @param channelName the channel to unsubscribe from
     */
    public void unsubscribeMapChannel(String channelName) {
        RelayMessage relayMessage = new RelayMessage();
        relayMessage.channelName = channelName;
        relayMessage.messageType = RelayMessageType.UNSUBSCRIBE_MAP;
        relayMessage.payload = null;
        String jString = g.toJson(relayMessage);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Deletes all data in a channel
     * @param channelName 
     */
    public void flushChannel(String channelName) {
        RelayMessage relayMessage = new RelayMessage();
        relayMessage.channelName = channelName;
        relayMessage.messageType = RelayMessageType.FLUSH;
        relayMessage.payload = null;
        String asJSON = g.toJson(relayMessage);
        publishChannel.write(asJSON + "\n");
    }
    
    /**
     * Deletes all data in a map channel
     * @param channelName 
     */
    public void flushMapChannel(String channelName) {
        RelayMessage relayMessage = new RelayMessage();
        relayMessage.channelName = channelName;
        relayMessage.messageType = RelayMessageType.FLUSH_MAP;
        relayMessage.payload = null;
        String asJSON = g.toJson(relayMessage);
        publishChannel.write(asJSON + "\n");
    }
    
    /**
     * Returns true if the client is currently connected to the server
     * @return 
     */
    public boolean isConnected() {
        return publishChannel != null && publishChannel.isOpen() && publishChannel.isConnected();
    }
    
    public boolean canPublish() {
        return isConnected();
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
    
}
