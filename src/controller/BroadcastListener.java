package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseConnection;
import model.Message;
import model.User;

public class BroadcastListener implements Runnable{

	private String localuser;
	public static final int LISTENING_PORT = 50005;
	private DatagramSocket ds;
	private InetAddress clientAddress;
	private String rcvdMessage;
	private ArrayList<String> listOfConnected = new ArrayList<String>();
	private volatile boolean isStopped ; 

	/** receiver	
	 * 
	 * @param localuser
	 * @param localport
	 */
	public BroadcastListener(String localuser) {
		this.localuser = localuser ; 

		try {
			this.ds = new DatagramSocket(LISTENING_PORT) ; 
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
			} catch (IOException e) { 
				if (isStopped) System.err.println("ds closed");
				else e.printStackTrace();}

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
				if (localuser.contentEquals(pseudo)) {
					sendNotUnique(port);
				}
			} else if (type.equals("1"))  {
				if (!localuser.equals(pseudo)) {
					System.out.println("type 1 = new connection");

					User newUser = new User(pseudo,this.clientAddress);
					handlePseudoChanging(newUser);

					this.listOfConnected.add(pseudo);
					sendConnected(LISTENING_PORT);
				}

			} else if (type.equals("2")) {
				System.out.println("type 2 = user leaving");
				//remove from user list
				this.listOfConnected.remove(pseudo);


			} else if (type.equals("3")) {
				if (!localuser.equals(pseudo)) {
					System.out.println("type 3 = connection");
					User newUser = new User(pseudo,this.clientAddress);
					this.listOfConnected.add(pseudo);
					//add in user list
					handlePseudoChanging(newUser);
				}
			}
		}		
	}

	private void handlePseudoChanging(User newUser) {
		String pseudo = newUser.getPseudo() ; 

		String previous = DatabaseConnection.selectUser(this.clientAddress) ;
		System.out.println("pseudo "+previous);
		String ip = DatabaseConnection.selectIp(pseudo) ; 
		System.out.println("ip "+ip);


		if (!ip.equals("")) {
			DatabaseConnection.deleteUser(pseudo);
		}
		if (previous.equals("")) {
			DatabaseConnection.insertUser(newUser);
		} else {
			if (!previous.equals(pseudo)) {
				DatabaseConnection.changePseudoInUsers(newUser, previous);
			} 
		}
	}

	private void sendConnected(int port) {
		String answer = "3"+this.localuser ;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, port);
		try {
			this.ds.send(outPacket) ; 
			System.out.println("response connected sent");
		} catch (IOException e) {System.err.println("send() failed");}
	}

	private void sendNotUnique(int port) {
		String answer = this.localuser;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, port);
		try {
			this.ds.send(outPacket) ; 
			System.out.println("response not unique sent");
		} catch (IOException e) {System.err.println("send() failed");}
	}

	public ArrayList<String> getListOfConnected() {
		return listOfConnected;
	}

	public void setListOfConnected(ArrayList<String> listOfConnected) {
		this.listOfConnected = listOfConnected;
	}

}


