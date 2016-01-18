package strawn.longleaf.relay.client;

/**
 *
 * @author David Strawn
 * 
 * This interface is implemented by clients that connect to the relay server
 * 
 */
public interface Connector {
    
    public abstract boolean reconnect();
    public abstract boolean isConnected();
    public abstract void onConnection();
    public abstract void onFailedConnection();
    public abstract void disconnect();
    public abstract void configAndConnect(String address, int port);
    
}
