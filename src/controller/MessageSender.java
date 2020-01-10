package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;

import model.Message;
import model.User;

public class MessageSender implements Runnable {
	
	private Message message ; 
	private User dest;
	

	public MessageSender(User dest, Message message) {
		this.dest = dest;
		this.message = message ; 
		Thread th = new Thread(this); 
		th.start();
	}
	
	public void run() {
		System.out.println("Conversation opened");
		try {

			this.send();
			this.dest.getSckt().close();

		} catch(IOException e) {e.printStackTrace();}
	}

	public void send() {
		
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(this.dest.getSckt().getOutputStream());
			
			oos.writeObject(message.getContent());
			System.out.println("message envoyé : "+ message.getContent());
			
			oos.close();

		}catch (IOException e) {System.out.println("Error");}
	}


	public void setMessage(Message message) {
		this.message = message ; 
	}


}
