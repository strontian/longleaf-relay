package strawn.longleaf.relay.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author David Strawn
 * 
 * This class loads configuration from the file system.
 * 
 */
public class RelayConfigLoader {
    
    private static final String serverConfigLocation = "./resources/serverconfig.properties";
    private static final String clientConfigLocation = "./resources/clientconfig.properties";
    
    private final Properties properties;
    
    public RelayConfigLoader() {
        properties = new Properties();
    }
    
    public void loadServerConfig() throws IOException {
        loadConfigFromPath(serverConfigLocation);
    }
    
    public void loadClientConfig() throws IOException {
        loadConfigFromPath(clientConfigLocation);
    }
    
    /**
     * Use this method if you want to use a different location for the configuration.
     * @param configFileLocation - location of the configuration file
     * @throws IOException 
     */
    public void loadConfigFromPath(String configFileLocation) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(configFileLocation);
        properties.load(fileInputStream);
        fileInputStream.close();
    }
    
    public int getPort() {
        return Integer.parseInt(properties.getProperty("port"));
    }
    
    public String getHost() {
        return properties.getProperty("host");
    }
    
}
