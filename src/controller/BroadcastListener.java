package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.DatabaseConnection;
import model.User;

/** Handles incoming UPD broadcasts
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class BroadcastListener implements Runnable{

	private String localuser;
	public static final int LISTENING_PORT = 50005;
	private DatagramSocket ds;
	private InetAddress clientAddress;
	private int clientPort; 
	private String rcvdMessage;
	private ArrayList<String> listOfConnected = new ArrayList<String>();
	private volatile boolean isStopped ;

	/** Constructor. Starts the listening thread and instantiates a DatagramSocket with LISTENING_PORT
	 * 
	 * @param localuser pseudo
	 */
	public BroadcastListener(String localuser) {
		this.localuser = localuser ; 

		try {
			this.ds = new DatagramSocket(LISTENING_PORT) ; 
		} catch (IOException e) {e.printStackTrace();}
		Thread th = new Thread(this); 
		th.start();
	}

	/** Launches waitForBroadcast()
	 * 
	 */
	public void run() {
		this.waitForBroadcast();
	}

	/** Listens for incoming broadcasts and calls response() to process an answer.
	 *  Carries on listening unless the stop() method is called. 
	 * 
	 */
	private void waitForBroadcast() {

		byte[] buf = new byte[256] ; 
		DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
		while (!isStopped) {
			try {
				this.ds.receive(inPacket);					
				System.out.println("received");
			} catch (IOException e) { 
				if (isStopped) System.err.println("ds closed");
				else e.printStackTrace();
			}

			this.clientAddress= inPacket.getAddress();
			this.clientPort = inPacket.getPort() ; 
			this.rcvdMessage = new String(inPacket.getData(), 0, inPacket.getLength()) ;

			System.out.println("BroadcastListener has received \""+rcvdMessage+"\" from "+this.clientAddress.getHostAddress());
			this.response();
		}
	}

	/**
	 * Stops the running thread by changing "isStopped" value and closing the DatagramSocket. 
	 */
	public void stop() {
		isStopped = true ; 
		try {
			ds.close();
		} catch (Exception e) {e.printStackTrace();}		
	}

	/**
	 * Matches the received message with four different types :
	 * pseudo unique, new connection, user_leaving
	 * and connected (can only be an answer to NEW_CONNECTION, can not be sent with a BroadcastSender)
	 * 
	 * Checks if the pseudo contained in the received message is equal to the localuser one. 
	 * If so, the message is not handled (except for type PSEUDO_UNIQUE).
	 */
	private void response() {
		String regex= "^([0-3])(\\w*)$" ; 
		Pattern p = Pattern.compile(regex) ;      
		Matcher m = p.matcher(this.rcvdMessage) ;    

		if(m.matches()){
			String type = m.group(1); 
			String pseudo = m.group(2) ;


			if (type.equals("0")) {
				//type 0 = is pseudo unique ?
				if (localuser.contentEquals(pseudo)) {
					sendNotUnique();
				}
			} else if (type.equals("1"))  {
				//type 1 = new connection
				if (!localuser.contentEquals(pseudo)) {
					User newUser = new User(pseudo,this.clientAddress);
					this.listOfConnected.add(pseudo);

					//delete first and insert after to avoid 
					//having two same pseudos or two same IPs in the DB
					DatabaseConnection.deleteUser(newUser);
					DatabaseConnection.insertUser(newUser);
					sendConnected();
				}
			} else if (type.equals("2")) {
				//type 2 = user leaving
				this.listOfConnected.remove(pseudo);

			} else if (type.equals("3")) {
				//type 3 = connected
				if (!localuser.contentEquals(pseudo)) {
					User newUser = new User(pseudo,this.clientAddress);
					this.listOfConnected.add(pseudo);

					//delete first and insert after to avoid 
					//having two same pseudos or two same IPs in the DB
					DatabaseConnection.deleteUser(newUser);
					DatabaseConnection.insertUser(newUser);

				}
			}	
		}
	}


	/** Answers to a NEW_CONNECTION message. 
	 * This message type is sent by a new user in the network, the other users say "Hi".
	 * 
	 */
	private void sendConnected() {
		String answer = "3"+this.localuser ;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, this.clientPort);
		try {
			this.ds.send(outPacket) ; 
		} catch (IOException e) {e.printStackTrace();}
	}

	/** Answers to a PSEUDO_UNIQUE message in case the pseudo is equal to localuser one. 
	 * 
	 */
	private void sendNotUnique() {
		String answer = this.localuser;
		DatagramPacket outPacket= new DatagramPacket(answer.getBytes(), answer.length(),this.clientAddress, LISTENING_PORT);
		try {
			this.ds.send(outPacket) ; 
		} catch (IOException e) {e.printStackTrace();}
	}

	/** Gets listOfConnected users 
	 * 
	 * @return list of connected users
	 */
	public ArrayList<String> getListOfConnected() {
		return listOfConnected;
	}

}


