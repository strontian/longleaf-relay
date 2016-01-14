package strawn.longleaf.relay.client;

import com.dstrawn.datamsgs.netty.json.JSONHandler;
import com.dstrawn.datamsgs.netty.json.JSONPipelineFactory;
import com.dstrawn.datamsgs.netty.json.JSONPublisher;
import com.dstrawn.datamsgs.pojos.JSONWrapper;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 *
 * @author David Strawn
 */
public abstract class NettyJSONPublisher extends JSONHandler implements JSONPublisher, Connector {
    
    protected ClientBootstrap bootstrap;
    protected Channel publishChannel;
    protected String host;
    protected int port;
    
    public NettyJSONPublisher(Executor boss, Executor work) {
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(boss, work, 1));
        bootstrap.setPipelineFactory(new JSONPipelineFactory(this));
        bootstrap.setOption("tcpNoDelay", "true");
        bootstrap.setOption("receiveBufferSize", 1048576);
        bootstrap.setOption("keepAlive", true);
    }
    
    public NettyJSONPublisher() {
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
        JSONWrapper jw = new JSONWrapper();
        jw.key = key;
        jw.messageType = "DATA";
        jw.payload = s;
        String jString = g.toJson(jw);
        publishChannel.write(jString + "\n");
    }
    
    /**
     * Subscribes to data
     * @param key - the name of the stream to subscribe
     */
    public void subData(String key) {
        JSONWrapper jw = new JSONWrapper();
        jw.key = key;
        jw.messageType = "SUBSCRIBE";
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
