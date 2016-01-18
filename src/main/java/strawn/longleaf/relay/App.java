package strawn.longleaf.relay;

import java.io.IOException;
import strawn.longleaf.relay.examples.ExampleReceiver;
import strawn.longleaf.relay.examples.ExampleSender;
import strawn.longleaf.relay.server.RelayServer;
import strawn.longleaf.relay.util.RelayConfigLoader;

/**
 * This is the entry point for the server. With no arguments or when run with argument 'server',
 * it launches the relay server.
 * 
 * It also accepts arguments: sender, receiver
 * 
 * -When run with argument 'sender', it creates an example client that sends data to the server.
 * -When run with argument 'receiver', it creates an example client that connects to the server and listens for data.
 * 
 */
public class App {
    
    public static void main(String[] args) {
        String arg = "server";
        if(args.length > 0) {
            arg = args[0];
        }
            
        RelayConfigLoader configLoader = new RelayConfigLoader();
        
        if(arg.equals("server")) {
            RelayServer relayServer = new RelayServer();
            try {
                configLoader.loadServerConfig();
                relayServer.connect(configLoader.getPort());
                System.out.println("Started server on port:" + configLoader.getPort());
            } catch (IOException ex) {
                System.out.println("Exception starting server:" + ex.getMessage());
            }
        }else if(arg.equals("sender")) {
            ExampleSender exampleSender = new ExampleSender();
            try {
                configLoader.loadClientConfig();
                exampleSender.configAndConnect(configLoader.getHost(), configLoader.getPort());
            } catch (IOException ex) {
                System.out.println("Exception starting example sender:" + ex.getMessage());
            }
        }else if(arg.equals("receiver")) {
            ExampleReceiver exampleReceiver = new ExampleReceiver();
            try {
                configLoader.loadClientConfig();
                exampleReceiver.configAndConnect(configLoader.getHost(), configLoader.getPort());
            } catch (IOException ex) {
                System.out.println("Exception starting example receiver:" + ex.getMessage());
            }
        }
        
    }
    
}
