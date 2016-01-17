package strawn.longleaf.relay.messages;

/**
 *
 * @author David Strawn
 */
public enum RelayMessageType {
    SUBSCRIBE,
    DATA,
    END_DATA,
    FLUSH,
    PUBLISH,
    REPLACE,
    END_REFRESH
}
