package controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	public void send() {
		try {
			Socket s= new Socket("127.0.0.1", 12454) ; 
			OutputStream out = s.getOutputStream(); 
			out.write(2);
			s.close();
		}catch (IOException e) {e.printStackTrace();}
		
		
	}

}
