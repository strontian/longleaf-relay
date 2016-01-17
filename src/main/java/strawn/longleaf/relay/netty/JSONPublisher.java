package strawn.longleaf.relay.netty;

/**
 *
 * @author David Strawn
 */
public interface JSONPublisher {
    
    public abstract void publishJSON(Object o, String key);
    public abstract boolean canPublish();
    
}
