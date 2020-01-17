package controller;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import database.DatabaseConnection;
import model.Message;
import model.User;

public class MessageWaiter implements Runnable {
	private ServerSocket servSocket; 
	private Socket link ;
	private volatile boolean isStopped ; 
	public static final int CONVERSATION_PORT = 12347;
	
	public MessageWaiter() {
		Thread th = new Thread(this); 
		th.start();
	}

	public void run() {
		System.out.println("Conversation opened");

		this.waitForMessage();
	}

	public void stop() {
		this.isStopped = true ; 
		try {
			if (this.link!=null)
				this.link.close();
			this.servSocket.close();
		} catch (Exception e) {e.printStackTrace();}
	}


	private void waitForMessage() {
		try {

			this.servSocket = new ServerSocket(CONVERSATION_PORT);
			while(!isStopped) {
			
				this.link = this.servSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(link.getInputStream());
				Message mes = (Message) ois.readObject();
				
				ois.close();
				System.out.println("received "+mes.getContent()+" "+mes.getSrc().getPseudo());
				DatabaseConnection.insertMessage(mes);
			}

		}catch (Exception e) {
			if (isStopped) System.err.println("message waiter closed");
			else e.printStackTrace();}
	}


}

























