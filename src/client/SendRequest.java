/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */
package client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.swing.*;

import org.json.JSONObject;

/**
 * This class is for sending requests to dictionary server to search, add or
 * delete word. This also displays meanings of word when user searches or add word.
 */
public class SendRequest {
	
	// Sending request for query method
	public void search(Socket socket, String word, JTextField searchField, JTextArea resultInput) {
		if (sendMessage(socket, "query", word, "")) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String input = in.readLine();
				JSONObject message = new JSONObject(input);
				
				if (message.getString("status").equals("success")) {
					resultInput.setText("");
					
					int i = 1;
					for (Object m : message.getJSONArray("meanings")) {
						resultInput.append(Integer.toString(i) + ". " + m.toString() + "\n");
						i++;
					}
				} else {
					JOptionPane.showMessageDialog(
							null,
			                "\"" + message.getString("word") + "\" not found.", "Error",
			                JOptionPane.ERROR_MESSAGE);
					resultInput.setText("");
				}
				
				searchField.setText("");
			} catch (IOException e) {
				displayError();
				System.out.println(e.getMessage());
			}
		}
	}
	
	// Sending request for add method
	public void add(Socket socket, String word, String meaning, JTextField searchField, JTextArea resultInput) {
		if (sendMessage(socket, "add", word, meaning)) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String input = in.readLine();
				JSONObject message = new JSONObject(input);
				
				if (message.getString("status").equals("success")) {
					JOptionPane.showMessageDialog(
							null,
			                "\"" + message.getString("word") + "\" successfully added.", "Info",
			                JOptionPane.INFORMATION_MESSAGE);						
				} else {
					JOptionPane.showMessageDialog(
							null,
			                "\"" + message.getString("word") + "\" already exists in the dictionary.", "Error",
			                JOptionPane.ERROR_MESSAGE);
				}
				
				resultInput.setText("");
				searchField.setText("");
			} catch (IOException e){
				displayError();
				System.out.println(e.getMessage());
			}
		}
	}
	
	// Sending request for remove method
	public void delete(Socket socket, String word, JTextField searchField, JTextArea resultInput) {
		if (sendMessage(socket, "remove", word, "")) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String input = in.readLine();
				JSONObject message = new JSONObject(input);
				
				if (message.getString("status").equals("success")) {						
					JOptionPane.showMessageDialog(
							null,
			                "\"" + message.getString("word") + "\" successfully deleted.", "Info",
			                JOptionPane.INFORMATION_MESSAGE);
					
				} else if (message.getString("status").equals("fail")) {
					JOptionPane.showMessageDialog(
							null,
			                "\"" + message.getString("word") + "\" not found.", "Error",
			                JOptionPane.ERROR_MESSAGE);
					resultInput.setText("");
				} else {
					JOptionPane.showMessageDialog(
							null,
			                "Delete cancelled.", "Info",
			                JOptionPane.INFORMATION_MESSAGE);
					resultInput.setText("");
				}
				
				resultInput.setText("");
				searchField.setText("");
			} catch (IOException e) {
				displayError();
				System.out.println(e.getMessage());
			}
		}
	}
	
	// Let user know connection error happens.
	private void displayError() {
		JOptionPane.showMessageDialog(
				null,
                "Connection Failed.", "Error",
                JOptionPane.ERROR_MESSAGE);
	}
	
	private boolean sendMessage(Socket socket, String method, String word, String meaning) {
		JSONObject json = new JSONObject();
		json.put("method", method);
		json.put("word", word);
		json.put("meaning", meaning);
		
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			out.write(json.toString() + "\n");
			out.flush();
			return true;
		} catch (IOException e) {
			displayError();
			System.out.println(e.getMessage());
			return false;
		}
	}
}
