package strawn.longleaf.relay.client;

/**
 *
 * @author David Strawn
 */
public interface Connector {
    
    public abstract boolean reconnect();
    public abstract boolean isConnected();
    public abstract void onConnection();
    public abstract void onFailedConnection();
    public abstract void close();
    public abstract void configAndConnect(String address, int port);
    
}
