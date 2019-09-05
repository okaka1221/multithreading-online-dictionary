/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */

package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

import org.json.JSONObject;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * This class is for setting up dictionary server and waiting for 
 * clients request. Server initializes thread pool and creates thread
 * per connection.
 * It also logs connection information and thread information.
 */
public class DictionaryServer implements DateTime {
	// Default pool size is 5.
	private static int threadNum = 5;
	
	public static void main(String[] args) throws CmdLineException, IOException {
        ArgBean bean = new ArgBean();
        // Parse parameters.
        CmdLineParser parser = new CmdLineParser(bean);
        
        try {
        	parser.parseArgument(args);
        	
        	// Check if dictionary file exists or not.
        	checkFilePath(bean.getDictFile());
        	
        	// Open a server socket
        	ServerSocket serverSocket = new ServerSocket(bean.getPort());
        	
        	// Create a frame for a server
        	ServerFrame frame = new ServerFrame(bean, threadNum);
        	
        	// Initialize thread pool.
        	ThreadPool threadPool = new ThreadPool(threadNum, frame);
        	
        	// Number of clients.
        	int i = 1;
        	while (true) {
        		// Wait for client requests until server is closed.
        		Socket clientSocket = serverSocket.accept();
        		
        		// Display server information.
        		frame.displayServerInfo(clientSocket, i);
        		
        		// Let client know that server creating thread and pass it to thread pool.
        		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    			out.write("waiting\n");
    			out.flush();
    			
    			// Create task for a client and enqueue it in threads queue.
        		DictionaryTask task = new  DictionaryTask(clientSocket, i, bean.getDictFile(), frame);
        		threadPool.enqueueTask(task);
        		i++;
        	}
		} 
        catch (CmdLineException e) {
			System.out.println(e.getMessage());
			parser.printUsage(System.out);
			System.exit(0);
		} 
        catch (IOException e) {
			JOptionPane.showMessageDialog(
					null, 
					"Error Setting up Socket.", "Error",
			        JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
			System.exit(0);
		}
    }
	
	// Check if dictionary file exists.
	private static void checkFilePath(File dictFile) {
		if (!dictFile.exists()) {
        	int ans = JOptionPane.showConfirmDialog(
    				null, 
    				"Dictionary file not fount.\nDo you want to create new file?", 
    				"Confirm",
    				JOptionPane.YES_NO_OPTION);
        	
    		if (ans == JOptionPane.YES_OPTION) {
    			// Create new dictionary file.
    			try {
    				FileWriter file = new FileWriter(dictFile, false);
    				file.write(new JSONObject().toString());
    				file.flush();
    				file.close();
    				
					JOptionPane.showMessageDialog(
	    					null,
	    	                "New dictionary file is successfully created.", "Info",
	    	                JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(
	    					null,
	    	                "Failed to create new dictionary file.\nServer is closed.", "Error",
	    	                JOptionPane.ERROR_MESSAGE);
					System.out.println("Input correct path to dictionary file.");
					System.exit(0);
			 	}
    		} else {
    			// Close server and exit.
		 		JOptionPane.showMessageDialog(
    					null,
    	                "Server is closed.", "Plain",
    	                JOptionPane.PLAIN_MESSAGE);
		 		System.out.println("Input correct path to dictionary file.");
				System.exit(0);
		 	}
    		
        }
	}
}
