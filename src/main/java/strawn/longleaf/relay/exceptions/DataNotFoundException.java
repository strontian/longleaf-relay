package strawn.longleaf.relay.exceptions;

/**
 *
 * @author David Strawn
 */
public class DataNotFoundException extends Exception {
    private final String channel;
    
    public DataNotFoundException(String channel) {
        this.channel = channel;
    }
    
    @Override
    public String getMessage() {
        return "Data not found for channel: " + channel;
    }
}
