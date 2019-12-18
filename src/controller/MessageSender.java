package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MessageSender implements Runnable {
	private Socket link ;
	private String message = "default"; 

	

	public MessageSender() {
		Thread th = new Thread(this); 
		th.start();
	}
	
	public void run() {
		System.out.println("Conversation opened");
		try {

			this.send(message);
			this.link.close();

		} catch(IOException e) {e.printStackTrace();}
	}

	public void send(String message) {
		
		try {
			this.link = new Socket("127.0.0.1",12543);
			ObjectOutputStream oos = new ObjectOutputStream(link.getOutputStream());
			oos.writeObject(message);
			System.out.println("message envoyé : "+ message);
			oos.close();

		}catch (IOException e) {System.out.println("Error");}
	}


	public void setMessage(String message) {
		this.message = message ; 
	}


}
