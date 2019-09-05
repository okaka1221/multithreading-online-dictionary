/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */

package server;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

import javax.swing.*;


/**
 * This class is for displaying server information and thread 
 * information. Server information contains when clients connect 
 * to the server and close connection.
 * Thread information contains when each threads in thread pool 
 * starts and finish.
 */
public class ServerFrame extends JFrame implements DateTime {
	private JPanel infoPanel = new JPanel();
	private JPanel serverPanel = new JPanel();
	private JTextArea serverInfo = new JTextArea();
	private JScrollPane scrollServerInfo = new JScrollPane(serverInfo);
	private JPanel threadPanel = new JPanel();
	private JTextArea threadInfo = new JTextArea();
	private JScrollPane scrollThreadInfo = new JScrollPane(threadInfo);
	private JPanel buttonPanel = new JPanel();
	private JButton button = new JButton();
	
	public JTextArea getInfo() {
		return serverInfo;
	}
	
	public JTextArea getThreadInfo() {
		return threadInfo;
	}
	
	public ServerFrame(ArgBean bean, int threadNum) {
		getContentPane().setLayout(new FlowLayout());
		setTitle("Online Dictionary - Server");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
               
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
    			closingAction();
    		}
		});
        
        infoPanel.setLayout(new GridLayout(1, 2));
                
        // Panel for server information.
        serverInfo.setEditable(false);
        scrollServerInfo.setPreferredSize(new Dimension(350, 400));
        serverPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        serverPanel.add(scrollServerInfo);
        serverPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel.add(serverPanel);
		
		// Panel for thread information.
		threadInfo.setEditable(false);
		threadInfo.append("================\n");
		threadInfo.append(threadNum + " threads in thread pool\n");
		threadInfo.append("================\n\n");
        scrollThreadInfo.setPreferredSize(new Dimension(300, 400));
        threadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        threadPanel.add(scrollThreadInfo);
        threadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		infoPanel.add(threadPanel);
		
        displayOpenMessage(bean);
        add(infoPanel);
        
        // Button for closing server and exit.
        button.setText("Exit");
        button.setPreferredSize(new Dimension(100, 60));
        button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closingAction();
			}
		});
        buttonPanel.add(button);
        add(buttonPanel);
                
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	// Ask users if they really want to close server.
	private void closingAction() {
		int ans = JOptionPane.showConfirmDialog(
				null, 
				"Do you want to exit?", 
				"Confirm",
				JOptionPane.YES_NO_OPTION);
		
		if (ans == JOptionPane.YES_OPTION) {
			System.out.println("Server is closed.");
			System.exit(0);
		}
	}
	
	// Log initial server information.
	private void displayOpenMessage(ArgBean bean) {
		serverInfo.append("Open at: " + getDateTime() + "\n");
		serverInfo.append("Port: " + bean.getPort() + "\n\n");
	}
	
	// Log client information.
	public void displayServerInfo(Socket clientSocket, int counter) {
		serverInfo.append("==============================\n");
		serverInfo.append("Client number: " + counter + "\n");
		serverInfo.append("Client accepted at: " + getDateTime() + "\n");
		serverInfo.append("Remote Hostname" + clientSocket.getInetAddress().getHostName() + "\n");
		serverInfo.append("Remote Port Number: " + clientSocket.getPort() + "\n");
		serverInfo.append("==============================\n\n");
	}
}
