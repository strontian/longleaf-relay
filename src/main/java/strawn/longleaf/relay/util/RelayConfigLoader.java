package strawn.longleaf.relay.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author David Strawn
 */
public class RelayConfigLoader {
    
    private static final String serverConfigLocation = "serverconfig.properties";
    private static final String clientConfigLocation = "clientconfig.properties";
    
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
    
    public void loadConfigFromPath(String configFileLocation) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(configFileLocation);
        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("server config file:'" + configFileLocation + "' not found.");
        }
        inputStream.close();
    }
    
    public int getPort() {
        return Integer.parseInt(properties.getProperty("port"));
    }
    
    public String getHost() {
        return properties.getProperty("host");
    }
    
}
