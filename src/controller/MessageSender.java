package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import database.DatabaseConnection;
import model.Message;

/** Handles message sending through TCP. Used in the ChatFrame. 
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class MessageSender implements Runnable {

	private Message message ; 

	/** Constructor for MessageSender. Starts a new thread with the given message.
	 * 
	 * @param message
	 */
	public MessageSender(Message message) {
		this.message = message ; 
		Thread th = new Thread(this); 
		th.start();
	}

	/** Sends the message through TCP to the destination IP contained in the message 
	 * and to CONVERSATION_PORT. Adds the Message to the DB. 
	 * If nobody is listening, show an JOptionPane. 
	 * 
	 */
	public void run() {
		try {
			Socket s = new Socket(message.getDest().getIp(), MessageWaiter.CONVERSATION_PORT) ; 
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream()); 
			message.getSrc().setIp(getLocalIp());
			oos.writeObject(message);
			System.out.println("MessageSender run() sent this : "+ message.getContent());
			DatabaseConnection.insertMessage(message);
			oos.close();
			s.close();
		}catch (IOException e) {JOptionPane.showMessageDialog(new JFrame(), "Your friend has disconnected :(");}
	}

	/** Starts a TCP connection with google.com just to be able to get the local IP 
	 * 
	 * @return the local IP address
	 */
	public static InetAddress getLocalIp() {
		@SuppressWarnings("resource")
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("google.com", 80));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		return socket.getLocalAddress();
	}
}
