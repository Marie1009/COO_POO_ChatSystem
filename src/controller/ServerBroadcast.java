package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.User;

public class ServerBroadcast implements Runnable {
	private DatagramSocket ds ; 
	private InetAddress clientAddress ; 
	static private int receiverPort = 50000; 
	
	
	private String rcvdMessage; 
	private User localuser; 
	private int mode; // 0 envoie un broadcast et 1 attend de recevoir un broadcast
	private int msgType;
	
	
	private ArrayList<User> listOfConnected = new ArrayList<User>();

	/** receiver
	 * 
	 * @param localuser
	 * @param localport
	 */
	public ServerBroadcast(User localuser) {
		this.localuser = localuser ; 
		this.mode = 1;
		
		try {
			this.ds = new DatagramSocket(receiverPort) ; 
		} catch (IOException e) {System.err.println("Datagram socket waiter not created");}
		Thread th = new Thread(this); 
		th.start();
	}
	
	/** sender
	 * 
	 * @param localuser
	 * @param msgType
	 * @param localport
	 */
	public ServerBroadcast(User localuser, int msgType) {
		this.localuser = localuser ; 
		this.mode = 0;
		this.msgType = msgType;
		try {
			this.ds = new DatagramSocket() ; 
		} catch (IOException e) {e.printStackTrace();}
		Thread th = new Thread(this); 
		th.start();
	}
	
	public void run() {
		if (this.mode==0) {
			String a = this.msgType + this.localuser.getPseudo();
			System.out.println(a);
			this.sendBroadcast(a);
			this.getBroadcastAnswer();
		}else {
			this.waitForBroadcast();
		}
	}
	
	
	public void waitForBroadcast() {
		
		byte[] buf = new byte[256] ; 
		DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
		try {
			System.out.println("ok");
			
		
			this.ds.receive(inPacket);					
			
			
			System.out.println("received2");
		} catch (IOException e) {System.err.println("receive() failed");}
		
		this.clientAddress= inPacket.getAddress();
		
		int port = inPacket.getPort() ; 
		this.rcvdMessage = new String(inPacket.getData(), 0, inPacket.getLength()) ;
		System.out.println(rcvdMessage);
		this.response(port); 
		this.ds.close();
	}
	
	public void getBroadcastAnswer() {
		try {
			try {
				ds.setSoTimeout(1000);   // set the timeout in millisecounds.
				byte[] buf = new byte[256] ; 
				DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
				ds.receive(inPacket); 
				String truc = new String(inPacket.getData(), 0, inPacket.getLength()) ;
				System.out.println(truc);
			}catch(SocketTimeoutException e1) {
				System.out.println("timeout reached");
				ds.close(); }
		}catch(IOException e) {e.printStackTrace();}
	}

	
	public void sendBroadcast(String message) {
		try {
		DatagramPacket outPacket= new DatagramPacket(message.getBytes(), message.length(),InetAddress.getByName("10.1.255.255"), receiverPort);
		ds.send(outPacket);

		byte[] buffer = new byte[256]; 
		DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);
		System.out.println("broadcast envoyé");
		}catch(Exception e) {e.printStackTrace();}
		
	}
	
	
	public void response(int port) {
		String regex= "^([0-2])(\\w+)$" ; 
		Pattern p = Pattern.compile(regex) ;      
		Matcher m = p.matcher(this.rcvdMessage) ;    
		
		 if(m.matches()){
			String type = m.group(1); 
			String pseudo = m.group(2) ; 
			if (type.equals("0")) {
				System.out.println("type 0 = pseudo unique ?");
				if (localuser.getPseudo().contentEquals(pseudo)) {
					sendNotUnique(port);
				}
			} else if (type.equals("1"))  {
				System.out.println("type 1 = new connection");

				this.listOfConnected.add(new User(pseudo,new Socket()));
				//add in user list
			} else if (type.equals("2")) {
				System.out.println("type 2 = user leaving");
				//remove from user list
			}
		}		
	}
	
	public void sendNotUnique(int port) {
		String answer = "AlreadyUsed: "+this.localuser.getPseudo() ;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, port);
		try {
			this.ds.send(outPacket) ; 
			System.out.println("response sent");
		} catch (IOException e) {System.err.println("send() failed");}
	}	
}
