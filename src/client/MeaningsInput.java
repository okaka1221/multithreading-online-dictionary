/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */
package client;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

import javax.swing.*;

/**
 * This class is for receive meanings as input from user and send 
 * request for adding new word to server.
 */
public class MeaningsInput extends JFrame{
	JPanel inputLabelPanel = new JPanel();
	JLabel inputLabel = new JLabel("Please type one meaning per line.");
	JPanel inputPanel = new JPanel();
	JTextArea inputArea = new JTextArea(8, 40);
	JScrollPane scrollInput = new JScrollPane(inputArea);
	JPanel buttonPanel = new JPanel();
	JButton confirm = new JButton();
	JButton cancel = new JButton();
	Socket socket;
	SendRequest dict;
	String word;
	JTextField searchField;
	JTextArea resultInput;
	
	public MeaningsInput(Socket socket, String word, SendRequest dict, JTextField searchField, JTextArea resultArea) {
		this.socket = socket;
		this.dict = dict;
		this.word = word;
		this.searchField = searchField;
		this.resultInput = resultArea;
		AddButtonActions(socket);
		
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Input meanings");
		setLocationRelativeTo(null);
        
		
		inputLabelPanel.add(inputLabel);
		inputLabelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(inputLabelPanel);
		
		inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		inputPanel.add(scrollInput);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(inputPanel);
		
		confirm.setText("Confirm");
		confirm.setPreferredSize(new Dimension(100, 60));
		buttonPanel.add(confirm);
		
		cancel.setText("Cancel");
		cancel.setPreferredSize(new Dimension(100, 60));
		buttonPanel.add(cancel);
		
		add(buttonPanel);
		
		pack();
		setResizable(false);
		setVisible(true);
	}

	// Call add function in DictionaryFunction class.
	private void AddButtonActions(Socket socket) {
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String meanings = inputArea.getText(); 
				if (meanings.equals("")) {
					JOptionPane.showMessageDialog(
							null,
			                "Please fill out meaning field.", "Error",
			                JOptionPane.ERROR_MESSAGE);
				} else {
					dict.add(socket, word, meanings, searchField, resultInput);
					setVisible(false);
					dispose();
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
	}
}
