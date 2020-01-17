package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;

import database.DatabaseConnection;
import model.BroadcastType;
import model.User;

public class BroadcastSender implements Runnable {
	private DatagramSocket ds ;  
	private BroadcastType msgType;
	private boolean pseudoUnique= true;

	private String pseudo;

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
		this.pseudo=pseudo;
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
			message = "0"+this.pseudo;
			System.out.println(message);
			this.sendBroadcast(message);
			break;
		case NEW_CONNECTION : 
			message = "1"+this.pseudo ; 
			System.out.println(message);
			this.sendBroadcast(message);
			break;
		case USER_LEAVING : 
			message = "2"+this.pseudo;
			System.out.println(message);
			this.sendBroadcast(message);
			break;
		}
	}


	public boolean getBroadcastAnswer() {
		boolean res=false; 
		try {
			try {
				ds.setSoTimeout(1000);   // set the timeout in millisecounds
				byte[] buf = new byte[256] ; 
				DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
				ds.receive(inPacket); 
				String msg = new String(inPacket.getData(), 0, inPacket.getLength()) ;
				System.out.println(msg);
				if (msg.equals(this.pseudo))
					res = false;
			}catch(SocketTimeoutException e1) {
				System.out.println("timeout reached");
				ds.close(); 
				res =  true;}
		}catch(IOException e) {e.printStackTrace();}
		return res ;
	}


	private void sendBroadcast(String message) {
		try {
			DatagramPacket outPacket= new DatagramPacket(message.getBytes(), message.length(),InetAddress.getByName("255.255.255.255"), BroadcastListener.LISTENING_PORT);
			ds.send(outPacket);
			byte[] buffer = new byte[256]; 
			DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);
			System.out.println("broadcast envoyé");
		}catch(Exception e) {e.printStackTrace();}

	}

	public boolean isPseudoUnique() {
		return pseudoUnique;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public void setPseudoUnique(boolean pseudoUnique) {
		this.pseudoUnique = pseudoUnique;
	}


}
