package strawn.longleaf.relay.messages;

/**
 *
 * @author David Strawn
 * 
 * All messages sent to the server must be 'wrapped' in this class. 
 */
public class RelayMessage {
    
    public RelayMessageType messageType;
    public String payload;
    public String channelName;
    public String messageKey;
    
    public RelayMessage(RelayMessageType messageType, String payload, String channelName, String messageKey) {
        this.messageType = messageType;
        this.payload = payload;
        this.channelName = channelName;
        this.messageKey = messageKey;
    }

    public RelayMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
