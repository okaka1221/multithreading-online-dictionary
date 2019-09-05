/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */

package client;

import java.io.File;
import org.kohsuke.args4j.Option;

/**
 * This class is for setting the parameters received from client's 
 * command line using external library "args4j".
 */
public class ArgBean {
	@Option(name = "-sa", required = true, usage = "Server Address")
    private String host;
	
	@Option(name = "-sp", required = true, usage = "Server Port number")
    private int port;
    
	public String getHost() {
    	return host;
    }
	
    public int getPort() {
    	return port;
    }
}
