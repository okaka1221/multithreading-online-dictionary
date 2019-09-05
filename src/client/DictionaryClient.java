/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */
package client;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.*;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * This class is for connecting clients to server.
 * This also initializes interactive interface to use dictionary functions.
 */
public class DictionaryClient {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		ArgBean bean = new ArgBean();
		// Parse parameters.
        CmdLineParser parser = new CmdLineParser(bean);
        
        try {
        	parser.parseArgument(args);
        	// Create a socket bounded to a specific server based on given host and port. 
        	Socket socket = new Socket(bean.getHost(), bean.getPort());
        	
        	// Initialize interactive interface.
        	new ClientFrame(socket);
		}
        catch (CmdLineException e) {
        	System.out.println(e.getMessage());
			parser.printUsage(System.out);
			System.exit(0);
		} 
        catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(
					null, 
					"Unknown Host.", "Error",
			        JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
        catch (SocketException e) {
			JOptionPane.showMessageDialog(
					null, 
					"Server soket is not opened.", "Error",
			        JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
        }
        catch (IOException e) {
			JOptionPane.showMessageDialog(
					null, 
					"Connection Failed", "Error",
			        JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
        }
	}
}
