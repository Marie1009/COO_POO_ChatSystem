package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.User;

public class BroadcastListener implements Runnable{

	private User localuser;
	private int port = 50002;
	private DatagramSocket ds;
	private InetAddress clientAddress;
	private String rcvdMessage;
	private ArrayList<User> listOfConnected = new ArrayList<User>();
	private volatile boolean isStopped ; 
	
	/** receiver	
	 * 
	 * @param localuser
	 * @param localport
	 */
	public BroadcastListener(User localuser) {
		this.localuser = localuser ; 

		try {
			this.ds = new DatagramSocket(this.port) ; 
		} catch (IOException e) {e.printStackTrace();}
		Thread th = new Thread(this); 
		th.start();
	}

	public void run() {
		this.waitForBroadcast();
	}

	private void waitForBroadcast() {

		byte[] buf = new byte[256] ; 
		DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
		while (!isStopped) {
			try {
				this.ds.receive(inPacket);					
				System.out.println("received");
			} catch (IOException e) {System.err.println("receive() failed");}

			this.clientAddress= inPacket.getAddress();

			int port = inPacket.getPort() ; 
			this.rcvdMessage = new String(inPacket.getData(), 0, inPacket.getLength()) ;
			System.out.println(rcvdMessage);
			this.response(port); 
		}

	}

	public void stop() {
		isStopped = true ; 
		try {
			ds.close();
			System.out.println("socket ds closed");
		} catch (Exception e) {e.printStackTrace();}		
	}
	
	private void response(int port) {
		String regex= "^([0-3])(\\w*)$" ; 
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
				
			} else if (type.equals("3")) {
				System.out.println("type 3 = connected users ?");
				this.sendConnected(port);

			}
		}		
	}

	private void sendConnected(int port) {
		String answer = this.localuser.getPseudo() ;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, port);
		try {
			this.ds.send(outPacket) ; 
			System.out.println("response connected sent");
		} catch (IOException e) {System.err.println("send() failed");}
	}

	private void sendNotUnique(int port) {
		String answer = this.localuser.getPseudo() ;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, port);
		try {
			this.ds.send(outPacket) ; 
			System.out.println("response not unique sent");
		} catch (IOException e) {System.err.println("send() failed");}
	}
	
	public ArrayList<User> getListOfConnected() {
		return listOfConnected;
	}

	public void setListOfConnected(ArrayList<User> listOfConnected) {
		this.listOfConnected = listOfConnected;
	}

}


