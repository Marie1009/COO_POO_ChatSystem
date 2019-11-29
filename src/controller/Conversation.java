package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Conversation implements Runnable {
	private ServerSocket servSocket; 
	private Socket link ;
	private int mode ; 
	private String message = "default"; 
	
	public void run() {
		System.out.println("Conversation opened");
		if (mode==0) {
			this.waitForMessage();
		} else {
			this.send(this.message);
		}
	}
	
	public Conversation(int mode) {
		this.mode = mode ; 
		try {
			this.servSocket = new ServerSocket(1234);
		}catch (IOException e) {e.printStackTrace();}
		Thread th = new Thread(this); 
		th.start();
	}
	public void waitForMessage() {
		try {
			this.link = this.servSocket.accept();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(link.getInputStream()));
			char[] cbuf = new char[20] ; 
			br.read(cbuf) ; 

			System.out.println("message reçu :"+cbuf.toString());
		}catch (IOException e) {System.out.println("Error");}
	}

	public void send(String message) {
		try {
			this.link = new Socket("127.0.0.1",1234);
			
			PrintWriter out = new PrintWriter(link.getOutputStream(),true); 
			out.write(message);


		}catch (IOException e) {System.out.println("Error");}
	}

	public void closeChat() {
		try {
			this.link.close();
		}catch(IOException e) {System.err.println("Error on close()");}
		
	}
	
	public void setMessage(String message) {
		this.message = message ; 
	}
}

























