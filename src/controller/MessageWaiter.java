package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import model.User;

public class MessageWaiter implements Runnable {
	private ServerSocket servSocket; 
	private Socket link ;

	public void run() {
		System.out.println("Conversation opened");
		try {

			this.waitForMessage();
			this.servSocket.close();


		} catch(IOException e) {e.printStackTrace();}
	}

	public MessageWaiter() {
		Thread th = new Thread(this); 
		th.start();
	}
	public void waitForMessage() {
		try {

			this.servSocket = new ServerSocket(12543);
			while(true) {
				this.link = this.servSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(link.getInputStream());
				String mes = (String) ois.readObject();
				ois.close();
				System.out.println(mes);
			}

		}catch (Exception e) {e.printStackTrace();}
		//catch (InterruptedException ie) {this.servSocket.close();}
	}


}

























