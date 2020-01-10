package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import database.DatabaseConnection;
import model.BroadcastType;
import model.User;

public class BroadcastSender implements Runnable {
	private DatagramSocket ds ;  
	static private int receiverPort = 50003; 
	private User localuser; 
	private BroadcastType msgType;





	private boolean pseudoUnique= true;

	private String pseudoATester;



	/** sender
	 * 
	 * @param localuser
	 * @param msgType
	 * @param localport
	 */
	public BroadcastSender(BroadcastType msgType) {
		this.msgType = msgType;
		try {
			this.ds = new DatagramSocket() ; 
		} catch (IOException e) {e.printStackTrace();}
		Thread th = new Thread(this); 
		th.start();
	}

	public BroadcastSender(String pseudo,BroadcastType msgType) {
		this.msgType = msgType;
		this.pseudoATester=pseudo;
		try {
			this.ds = new DatagramSocket() ; 
		} catch (IOException e) {e.printStackTrace();}
		Thread th = new Thread(this); 
		th.start();
	}

	public void run() {
		String message="";
		switch(this.msgType) {
		case PSEUDO_UNIQUE : 
			message = "0"+this.pseudoATester;
			System.out.println(message);
			this.sendBroadcast(message);
			//this.getBroadcastAnswer();
			break;
		case NEW_CONNECTION : 
			if (this.localuser ==null) System.out.println("USER EMPTY"); 
			message = "1"+this.localuser.getPseudo();
			System.out.println(message);
			this.sendBroadcast(message);
			break;
		case USER_LEAVING : 
			if (this.localuser ==null) System.out.println("USER EMPTY"); 
			message = "2"+this.localuser.getPseudo();
			System.out.println(message);
			this.sendBroadcast(message);
			break;
		case CONNECTED_USERS : 
			message = "3";
			System.out.println(message);
			this.sendBroadcast(message);
			//this.getConnectedUsersAnswer();
			break;
		}
	}



	public void setLocaluser(User localuser) {
		this.localuser = localuser;
	}


	public boolean getBroadcastAnswer() {
		boolean res=false; 
		try {
			try {
				ds.setSoTimeout(1000);   // set the timeout in millisecounds
				byte[] buf = new byte[256] ; 
				DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
				ds.receive(inPacket); 
				String truc = new String(inPacket.getData(), 0, inPacket.getLength()) ;
				System.out.println(truc);
				res = false;
			}catch(SocketTimeoutException e1) {
				System.out.println("timeout reached");
				ds.close(); 
				res =  true;}
		}catch(IOException e) {e.printStackTrace();}
		return res ;
	}

	public ArrayList<String> getConnectedUsersAnswer() {
		ArrayList<String> users = new ArrayList<String>() ;

		while (true) {
			try {  // set the timeout in millisecounds
				ds.setSoTimeout(1000); 
				try {
					
					byte[] buf = new byte[256] ; 
					DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
					ds.receive(inPacket);
					String user = new String(inPacket.getData(), 0, inPacket.getLength()) ;
					System.out.println(user+" is connected");
					users.add(user) ; 
					User newUser = new User(user,new Socket(inPacket.getAddress(),MessageWaiter.CONVERSATION_PORT));
					DatabaseConnection.insertUser(newUser);
				}catch(SocketTimeoutException e1) {
					System.out.println("timeout reached");
					ds.close(); 
					break ;
				}
			}catch(IOException e) {e.printStackTrace();}
		}
		return users ;
	}


	private void sendBroadcast(String message) {
		try {
			DatagramPacket outPacket= new DatagramPacket(message.getBytes(), message.length(),InetAddress.getByName("10.1.255.255"), receiverPort);
			ds.send(outPacket);

			byte[] buffer = new byte[256]; 
			DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);
			System.out.println("broadcast envoyé");
		}catch(Exception e) {e.printStackTrace();}

	}

	public boolean isPseudoUnique() {
		return pseudoUnique;
	}

}
