/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */

package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import javax.swing.*;

/**
 * This class is for providing interactive interface for user.
 * User can search, add and delete word by pressing buttons.
 * When necessary, error messages are popped up for user.
 */
public class ClientFrame extends JFrame {
	private SendRequest dict = new SendRequest();
	private JPanel searchLabelPanel = new JPanel();
	private JLabel searchLabel = new JLabel("Type the word you want to look up.");
	private JPanel searchPanel = new JPanel();
	private JTextField searchField = new JTextField(20);
	private JPanel resultLabelPanel = new JPanel();
	private JLabel resultLabel = new JLabel("Meanings");
	private JPanel resultPanel = new JPanel();
	private JTextArea resultArea = new JTextArea(8, 40);
	private JScrollPane scrollResult = new JScrollPane(resultArea);
	private JPanel buttonPanel = new JPanel();
	private JButton search = new JButton();
	private JButton add = new JButton();
	private JButton delete = new JButton();
	private JPanel exitPanel = new JPanel();
	private JButton exit = new JButton();

	public ClientFrame(Socket socket) {
    	try {
    		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		JFrame waitFrame = new JFrame();
    		
			while (!in.readLine().equals("connected")) {
				waitFrame.setSize(300, 200);
				waitFrame.setTitle("Waiting for access...");
				waitFrame.setLayout(new GridLayout(2, 1));
				waitFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		        waitFrame.addWindowListener(new WindowAdapter() {
		        	@Override
		        	public void windowClosing(WindowEvent e) {
		    			closingAction();
		    		}
				});
				
		        // Inform that server is not ready for client.
				JPanel waitLabelPanel = new JPanel();
				JLabel waitLabel = new JLabel("<html>Please wait for a while.<br> or press Exit to exit now.</html>", SwingConstants.CENTER);
				waitLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				waitLabelPanel.add(waitLabel);
				waitFrame.add(waitLabelPanel);
				
				// Confirm to exit.
				JPanel noWaitButtonPanel = new JPanel();
				JButton notWaitButton = new JButton();
				notWaitButton.setText("Exit");
				notWaitButton.setPreferredSize(new Dimension(100, 60));
		        notWaitButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						closingAction();
					}
				});
		        
		        noWaitButtonPanel.add(notWaitButton);
		        waitFrame.add(noWaitButtonPanel);
		        
				waitFrame.setLocationRelativeTo(null);
				waitFrame.setResizable(false);
				waitFrame.setVisible(true);
			}
			
			waitFrame.dispose();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					null,
	                "Connection Failed.", "Error",
	                JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getMessage());
		}
    	
    	// Initialize actions for each button.
		AddButtonActions(socket);
		setTitle("Online Dictionary - Client");        
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Search field.
        searchLabelPanel.add(searchLabel);
        searchLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(searchLabelPanel);
        
        searchPanel.add(searchField);
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(searchPanel);
        
        // Text area for displaying meanings of word.
        resultLabelPanel.add(resultLabel);
        resultLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(resultLabelPanel);
        
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultArea.setEditable(false);
        resultPanel.add(scrollResult);
        resultPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(resultPanel);
        
        // Button for searching existing word.
        search.setText("Search");
        search.setPreferredSize(new Dimension(100, 60));
        buttonPanel.add(search);
        
        // Button for adding new word.
        add.setText("Add");
        add.setPreferredSize(new Dimension(100, 60));
        buttonPanel.add(add);
        
        // Button for deleting existing word.
        delete.setText("Delete");
        delete.setPreferredSize(new Dimension(100, 60));
        buttonPanel.add(delete);
        
        add(buttonPanel);
        
        // Button for exiting. 
        exit.setText("Exit");
        exit.setPreferredSize(new Dimension(100, 60));
        exitPanel.add(exit);
        add(exitPanel);
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
    			closingAction();
    		}
		});
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
	}
	
	// This method is for adding defined actions to each button.
	private void AddButtonActions(Socket socket) {
		search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String word = searchField.getText();
				if (checkWordField(word)) {
					dict.search(socket, word, searchField, resultArea);
				}
			}
		});
		
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String word = searchField.getText();
				if (checkWordField(word)) {
					new MeaningsInput(socket, word, dict, searchField, resultArea);
				}
			}
		});
		
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String word = searchField.getText();
				if (checkWordField(word)) {
					dict.delete(socket, word, searchField, resultArea);
				}
			}
		});
		
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closingAction();
			}
		});
	}
	
	// Check if search field is filled out.
	private boolean checkWordField(String word) {
		if (word.equals("")) {
			JOptionPane.showMessageDialog(
					null,
	                "Please fill out word field.", "Error",
	                JOptionPane.ERROR_MESSAGE);
		} else {
			return true;
		}
		
		return false;
	}
	
	// Confirm that user really wants to exit.
	private void closingAction() {
		int ans = JOptionPane.showConfirmDialog(
				null, 
				"Do you want to exit?", 
				"Confirm",
				JOptionPane.YES_NO_OPTION);
		
		if (ans == JOptionPane.YES_OPTION) {
			System.out.println("Socket is closed.");
			System.exit(0);
		}
	}
}
