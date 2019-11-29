package controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.*; 

public class Server {

	public void receive() {
		try {
			ServerSocket welcomeSocket = new ServerSocket(12454);
			Socket connectionSocket = welcomeSocket.accept();
			InputStream in = connectionSocket.getInputStream();
			System.out.println(in.readAllBytes().toString()) ; 
			welcomeSocket.close();
		}catch (IOException e) {e.printStackTrace();}
	}
}
