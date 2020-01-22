package controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import model.BroadcastType;

/** Permits sending different types of UDP broadcast 
 * (and handling some answers). 
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class BroadcastSender implements Runnable {

	private DatagramSocket ds ;  
	private BroadcastType msgType;
	private String pseudo;

	/** Constructor. Starts a new thread with the given message type and pseudo
	 * and instantiates a DatagramSocket.
	 * 
	 * @param pseudo
	 * @param msgType
	 */
	public BroadcastSender(String pseudo,BroadcastType msgType) {
		this.msgType = msgType;
		this.pseudo=pseudo;
		try {
			this.ds = new DatagramSocket() ; 
		} catch (IOException e) {e.printStackTrace();}
		Thread th = new Thread(this); 
		th.start();
	}


	/** Creates a message given type and calls for sendBroadcast()
	 * 
	 */
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

	/** Waits for answer to a PSEUDO_UNIQUE broadcast. If timeout without receiving anything,
	 * the pseudo is considered unique. 
	 * 
	 * @return true if pseudo unique, false otherwise
	 */
	public boolean isUnique() {
		boolean res=false; 
		try {
			try {
				ds.setSoTimeout(1000); 
				byte[] buf = new byte[256] ; 
				DatagramPacket inPacket = new DatagramPacket(buf, buf.length); 
				ds.receive(inPacket); 
				String msg = new String(inPacket.getData(), 0, inPacket.getLength()) ;
				if (msg.equals(this.pseudo))
					res = false;
			}catch(SocketTimeoutException e1) {
				ds.close(); 
				res =  true;}
		}catch(IOException e) {e.printStackTrace();}
		return res ;
	}

	/** Sends UDP broadcast to 255.255.255.255 IP address and LISTENING_PORT port. 
	 * 
	 * @param message to be sent
	 */
	private void sendBroadcast(String message) {
		try {
			DatagramPacket outPacket= new DatagramPacket(message.getBytes(), message.length(),InetAddress.getByName("255.255.255.255"), BroadcastListener.LISTENING_PORT);
			ds.send(outPacket);
		}catch(Exception e) {e.printStackTrace();}

	}
}
