package strawn.longleaf.relay;

import java.io.FileNotFoundException;
import java.io.IOException;
import strawn.longleaf.relay.server.RelayServer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import strawn.longleaf.relay.util.RelayConfigLoader;

/**
 * 
 * 
 */
public class App {
    
    public static void main(String[] args) {
        
        RelayServer relayServer = new RelayServer();
        RelayConfigLoader configLoader = new RelayConfigLoader();
        try {
            configLoader.loadServerConfig();
            relayServer.connect(configLoader.getPort());
        } catch (IOException ex) {
            System.out.println("Exception starting server:" + ex.getMessage());
        }
        
    }
    
}
