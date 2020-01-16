package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import database.DatabaseConnection;
import model.Message;

public class MessageSender implements Runnable {

	private Message message ; 


	public MessageSender(Message message) {
		this.message = message ; 
		Thread th = new Thread(this); 
		th.start();
	}

	public void run() {
		System.out.println("Conversation opened");
		try {
			Socket s = new Socket(message.getDest().getIp(), MessageWaiter.CONVERSATION_PORT) ; 
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

			oos.writeObject(message);
			System.out.println("message envoyé : "+ message.getContent());
			DatabaseConnection.insertMessage(message);
			oos.close();
			s.close();
		}catch (IOException e) {JOptionPane.showMessageDialog(new JFrame(), "Your friend has disconnected :(");}
	}

}
