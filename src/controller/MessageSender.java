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

	/** Sends 
	 * 
	 */
	public void run() {
		try {
			Socket s = new Socket(message.getDest().getIp(), MessageWaiter.CONVERSATION_PORT) ; 
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			System.out.println(s.getLocalAddress()); 
			message.getSrc().setIp(getLocalIp());
			oos.writeObject(message);
			System.out.println("message envoyé : "+ message.getContent());
			DatabaseConnection.insertMessage(message);
			oos.close();
			s.close();
		}catch (IOException e) {JOptionPane.showMessageDialog(new JFrame(), "Your friend has disconnected :(");}
	}

	public static InetAddress getLocalIp() {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("google.com", 80));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return socket.getLocalAddress();
	}
}
