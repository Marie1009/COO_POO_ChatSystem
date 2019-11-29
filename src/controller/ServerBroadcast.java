package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.User;

public class ServerBroadcast implements Runnable {
	private DatagramSocket ds ; 
	private InetAddress clientAddress ; 
	private int clientPort ; 
	private String message; 
	private User localuser; 
	
	public ServerBroadcast(User localuser) {
		this.localuser = localuser ; 
		try {
			this.ds = new DatagramSocket() ; 
		} catch (IOException e) {System.err.println("Datagram socket not created");}
	}
	
	public void run() {
		byte[] buf = new byte[256] ; 
		DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
		
		try {
			this.ds.receive(inPacket);
		} catch (IOException e) {System.err.println("receive() failed");}
		
		this.clientAddress= inPacket.getAddress();
		this.clientPort= inPacket.getPort() ; 
		this.message = new String(inPacket.getData(), 0, inPacket.getLength()) ;
		
		this.response(); 
	}
	
	public void response() {
		String regex= "^([0-2])(\\w+)$" ; 
		Pattern p = Pattern.compile(regex) ;      
		Matcher m = p.matcher(this.message) ;    
		
		 if(m.matches()){
			String type = m.group(1); 
			String pseudo = m.group(2) ; 
			if (type.equals("0")) {
				System.out.println("type 0 = pseudo unique ?");
				if (localuser.getPseudo().contentEquals(pseudo)) {
					sendNotUnique();
				}
			} else if (type.equals("1"))  {
				System.out.println("type 1 = new connection");
				//add in user list
			} else if (type.equals("2")) {
				System.out.println("type 2 = user leaving");
				//remove from user list
			}
		}		
	}
	
	public void sendNotUnique() {
		String answer = "AlreadyUsed:"+this.localuser.getPseudo() ;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, this.clientPort);
		try {
			this.ds.send(outPacket) ; 
		} catch (IOException e) {System.err.println("send() failed");}
	}	
}
