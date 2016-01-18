package strawn.longleaf.relay.exceptions;

/**
 *
 * @author David Strawn
 * 
 */
public class AlreadySubscribedException extends Exception {
    private final String channel;
    
    public AlreadySubscribedException(String channel) {
        this.channel = channel;
    }
    
    @Override
    public String getMessage() {
        return "The client is already subscribed to channel: " + channel;
    }
}
