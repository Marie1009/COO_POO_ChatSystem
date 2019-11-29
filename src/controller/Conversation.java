package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

			InputStream in = link.getInputStream();
			System.out.println(in.readAllBytes().toString()) ; 
			/*BufferedReader br = new BufferedReader(new InputStreamReader(link.getInputStream()));
			String m = br.readLine();
			System.out.println("message reçu :"+m);*/
			
		}catch (IOException e) {e.printStackTrace();}
	}

	public void send(String message) {
		try {
			this.link = new Socket("127.0.0.1",12543);

			PrintWriter out = new PrintWriter(link.getOutputStream(),true); 
			out.write(message);
			System.out.println("message envoyé : "+ message);

		}catch (IOException e) {System.out.println("Error");}
	}


	public void setMessage(String message) {
		this.message = message ; 
	}
}

























