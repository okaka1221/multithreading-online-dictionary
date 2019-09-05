/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */
package server;

import java.io.File;
import org.kohsuke.args4j.Option;

/**
 * This class is for setting the parameters received from server's 
 * command line using external library "args4j".
 */
public class ArgBean {
    @Option(name = "-p", required = true, usage = "Port number")
    private int port;
    
    @Option(name = "-df", required = true, usage = "Dictionaly file")
    private File dict;
    
    public int getPort() {
    	return port;
    }
    
    public File getDictFile() {
    	return dict;
    }
} 