package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageWaiter implements Runnable {
	private ServerSocket servSocket; 
	private Socket link ;
	private volatile boolean isStopped ; 

	public void run() {
		System.out.println("Conversation opened");

		this.waitForMessage();
	}

	public MessageWaiter() {
		Thread th = new Thread(this); 
		th.start();
	}

	public void stop() {
		this.isStopped = true ; 
		try {
			this.servSocket.close();
			this.link.close();
		} catch (Exception e) {e.printStackTrace();}
	}


	public void waitForMessage() {
		try {

			this.servSocket = new ServerSocket(12543);
			while(!isStopped) {
				this.link = this.servSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(link.getInputStream());
				String mes = (String) ois.readObject();
				ois.close();
				System.out.println("received "+mes);
			}

		}catch (Exception e) {e.printStackTrace();}
		//catch (InterruptedException ie) {this.servSocket.close();}
	}


}

























