package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.User;

public class Conversation implements Runnable {
	private ServerSocket servSocket; 
	private Socket link ;
	private int mode ; 
	private String message = "default"; 

	private ArrayList<User> listOfConnected = new ArrayList<User>();

	public void run() {
		System.out.println("Conversation opened");
		try {
			if (mode==0) {
				this.waitForMessage();
				this.servSocket.close();
	
			} else {
				this.send(message);
				this.link.close();
			}
		} catch(IOException e) {e.printStackTrace();}
	}

	public Conversation(int mode) {
		this.mode = mode ; 
		Thread th = new Thread(this); 
		th.start();
	}
	public void waitForMessage() {
		try {
		
			this.servSocket = new ServerSocket(12543);
			this.link = this.servSocket.accept();
			ObjectInputStream ois = new ObjectInputStream(link.getInputStream());
            //convert ObjectInputStream object to String
            String mes = (String) ois.readObject();
            ois.close();
			//InputStream in = link.getInputStream();
			//byte[] b = new byte[50];
			//in.read(b, 0, b.length) ; 
			System.out.println(mes);
			this.servSocket.close();
			
		}catch (Exception e) {e.printStackTrace();}
	}

	public void send(String message) {
		try {
			this.link = new Socket("127.0.0.1",12543);
            ObjectOutputStream oos = new ObjectOutputStream(link.getOutputStream());
            oos.writeObject(message);
			//OutputStream out = link.getOutputStream(); 
			//byte[] b = new byte[50];
			//b = message.getBytes() ; 
			//out.write(b, 0, b.length);
			System.out.println("message envoyé : "+ message);
			oos.close();

		}catch (IOException e) {System.out.println("Error");}
	}


	public void setMessage(String message) {
		this.message = message ; 
	}
}

























