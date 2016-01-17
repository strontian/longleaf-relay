package strawn.longleaf.relay.client;

import strawn.longleaf.relay.netty.JSONHandler;
import strawn.longleaf.relay.netty.JSONPipelineFactory;
import strawn.longleaf.relay.netty.JSONPublisher;
import strawn.longleaf.relay.messages.RelayMessage;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import strawn.longleaf.relay.messages.RelayMessageType;

/**
 *
 * @author David Strawn
 */
public abstract class RelayJSONClient extends JSONHandler implements JSONPublisher, Connector {
    
    protected ClientBootstrap bootstrap;
    protected Channel publishChannel;
    protected String host;
    protected int port;
    
    public RelayJSONClient(Executor boss, Executor work) {
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(boss, work, 1));
        bootstrap.setPipelineFactory(new JSONPipelineFactory(this));
        bootstrap.setOption("tcpNoDelay", "true");
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("keepAlive", true);
    }
    
    public RelayJSONClient() {
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), 1));
        bootstrap.setPipelineFactory(new JSONPipelineFactory(this));
        bootstrap.setOption("tcpNoDelay", "true");
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("keepAlive", true);
    }
    
    public boolean reconnect() {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        publishChannel = future.awaitUninterruptibly().getChannel(); 
        boolean ret = future.isSuccess();
        if(ret) {
            onConnection();
        }else {
            onFailedConnection();
        }
        return ret;
    }
    
    public void close() {
        publishChannel.close();
        bootstrap.releaseExternalResources();
    }
    
    public void configAndConnect(String host, int port) {
        this.host = host;
        this.port = port;
        reconnect();
    }
    
    public void writeJSON(String jString) {
        publishChannel.write(jString + "\n");
    }
    
    /**
     * publishes data to the server
     * @param o
     * @param key
     */
    public void publishJSON(Object o, String key) {
        String s = g.toJson(o);
        RelayMessage jw = new RelayMessage();
        jw.channelKey = key;
        jw.messageType = RelayMessageType.DATA;
        jw.payload = s;
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Subscribes to data
     * @param key - the name of the stream to subscribe
     */
    public void subData(String key) {
        RelayMessage jw = new RelayMessage();
        jw.channelKey = key;
        jw.messageType = RelayMessageType.SUBSCRIBE;
        jw.payload = "";
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    public boolean isConnected() {
        return publishChannel != null && publishChannel.isOpen() && publishChannel.isConnected();
    }
    
    public boolean canPublish() {
        return isConnected();
    }
    
}
