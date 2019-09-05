/**
 * Name: Kazuto Okamoto
 * Username: KOKAMOTO
 * Student ID: 1035484
 */

package server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.json.*;

/**
 * This class is for dictionary functions: query, add, remove.
 * The input streams contain methods(query, add, remove), a word and meaning.
 */
public class DictionaryTask extends Thread implements DateTime {
	private Socket clientSocket;
	private int clientNum;
	private File dictFile;
	private ServerFrame serverFrame;
	
	public DictionaryTask(Socket clientSocket, int clientNum, File dictFile, ServerFrame serverFrame) {
		this.clientSocket = clientSocket;
		this.clientNum = clientNum;
		this.dictFile = dictFile;
		this.serverFrame = serverFrame;
	}
	
	@Override
	public void run() {
		try {
			// Let a client know a client is allocated thread.
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			out.write("connected\n");
			out.flush();
			
			// Receive request from a client.
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputMessage;
			
			while ((inputMessage  = in.readLine()) != null) {
				JSONObject json = new JSONObject(inputMessage);
				String method = json.getString("method");
				String word = json.getString("word");
				JSONArray meanings = new JSONArray();
				for (String m : json.getString("meaning").split("\n")) {
					meanings.put(m);
				}
				
				switch (method) {
				// Search meanings of existing word. 
				case "query":
					JSONObject dict_s = openDictionary();
					JSONObject output_s = query(dict_s, word);
					out.write(output_s.toString() + "\n");
					out.flush();
					break;
				// Add a new word to the dictionary.
				case "add":
					JSONObject dict_a = openDictionary();
					JSONObject output_a = add(dict_a, word, meanings);
					out.write(output_a.toString() + "\n");
					out.flush();
					break;
				// Remove a existing word from the dictionary.	
				case "remove":
					JSONObject dict_d = openDictionary();
					JSONObject output_d = remove(dict_d, word);
					out.write(output_d.toString() + "\n");
					out.flush();
					break;
				}
			}
		}
		catch (SocketException e) {
			serverFrame.getInfo().append("Socket closed.\n");
			System.out.println(e.getMessage());
		}
		catch (IOException e) {
			serverFrame.getInfo().append("Error sending or getting data.\n");
			System.out.println(e.getMessage());
		}
		finally {
			// Close the socket when a client close socket.
			if (clientSocket != null) {
				try {
					clientSocket.close();
					serverFrame.getInfo().append("==============================\n");
					serverFrame.getInfo().append("Client connection number " + clientNum + " is over.\n");
					serverFrame.getInfo().append("Client socket is closed at " + serverFrame.getDateTime() + "\n");
					serverFrame.getInfo().append("==============================\n\n");
				} catch (IOException e) {
					serverFrame.getInfo().append("Error closing client socket.\n");
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	// Query a word in the dictionary.
	private synchronized JSONObject query(JSONObject dictObj, String word) {
		JSONObject result = new JSONObject();
		JSONArray meanings = new JSONArray();
		if (dictObj.has(word.toLowerCase())) {
			meanings = dictObj.getJSONArray(word.toLowerCase());
			result.put("status", "success");
		} else {
			result.put("status", "fail");
		}
		
		result.put("word", word.toLowerCase());
		result.put("meanings", meanings);
		return result;
	}
	
	// Add new word in the dictionary if the word does not exist.
	private synchronized JSONObject add(JSONObject dictObj, String word, JSONArray meanings) {
		JSONObject result = new JSONObject();
		result.put("word", word);
		
		if (query(dictObj, word).get("status") == "success") {
			result.put("status", "fail");
		} else {
			dictObj.put(word, meanings);
			result.put("status", "success");
			updateDictionary(dictObj);
			serverFrame.getInfo().append("Dictionary updated - ADD\n");
			serverFrame.getInfo().append("At " + getDateTime() + "\n\n");
		}
		
		return result;
	}
	
	// Remove existing word from the dictionary if the word exists.
	private synchronized JSONObject remove(JSONObject dictObj, String word) {
		JSONObject result = new JSONObject();
		result.put("word", word);
		
		if (query(dictObj, word).get("status") == "fail") {
			result.put("status", "fail");
		} else {
			JDialog dialog = new JDialog();
			dialog.setAlwaysOnTop(true);
			
			int ans = JOptionPane.showConfirmDialog(
					dialog, 
					"Do you really want to delete the word " + "\"" + word + "\" from dictionary?", 
					"Confirm",
					JOptionPane.YES_NO_OPTION);
			
			dialog.dispose();
			
			if (ans == JOptionPane.YES_OPTION) {
				dictObj.remove(word);
				result.put("status", "success");
				updateDictionary(dictObj);
						
				serverFrame.getInfo().append("Dictionary updated - REMOVE\n");
				serverFrame.getInfo().append("At " + getDateTime() + "\n\n");
			} else {
				result.put("status", "cancelled");
			}
		}
		
		return result;
	}
	
	// Open dictionary file (JSON file) and return json object.
	private JSONObject openDictionary() {
		try {
			JSONTokener read = new JSONTokener(new FileReader(dictFile));
			JSONObject dictJson = new JSONObject(read);
			return dictJson;
		}
		catch (FileNotFoundException e) {
			serverFrame.getInfo().append("Error the dictionary not found.\n");
			System.out.println(e.getMessage());
		}
		catch (JSONException e) {
			serverFrame.getInfo().append("Error parsing dictionary.\n");
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	// Update the dictionary.
	private void updateDictionary(JSONObject dictObj) {
		try {
			FileWriter file = new FileWriter(dictFile, false);
			file.write(dictObj.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			serverFrame.getInfo().append("Error updating dictionary.\n");
			System.out.println(e.getMessage());
		}
		
	}
}
