package controller;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import database.DatabaseConnection;
import model.Message;

/** Listens for incoming TCP packets. Used for messages in the ChatFrame.
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class MessageWaiter implements Runnable {
	private ServerSocket servSocket; 
	private Socket link ;
	private volatile boolean isStopped ; 
	public static final int CONVERSATION_PORT = 12347;

	/** Constructor. Starts a new thread.
	 * 
	 */
	public MessageWaiter() {
		Thread th = new Thread(this); 
		th.start();
	}

	/** Launches waitForMessage(). 
	 * 
	 */
	public void run() {
		this.waitForMessage();
	}

	/** Waits for incoming TCP packets. 
	 * Carries on listening unless the stop() method is called. 
	 * 
	 */
	private void waitForMessage() {
		try {

			this.servSocket = new ServerSocket(CONVERSATION_PORT);
			while(!isStopped) {
				this.link = this.servSocket.accept();
				ObjectInputStream ois = new ObjectInputStream(link.getInputStream());
				Message mes = (Message) ois.readObject();

				ois.close();
				System.out.println("MessageWaiter received this : "+mes.getContent()+"\nfrom : "+mes.getSrc().getPseudo());
				DatabaseConnection.insertMessage(mes);
			}

		}catch (Exception e) {
			if (isStopped) System.err.println("message waiter closed");
			else e.printStackTrace();}
	}

	/** Closes the Socket and the ServerSocket and allows waitForMessage() to stop. 
	 * 
	 */
	public void stop() {
		this.isStopped = true ; 
		try {
			if (this.link!=null)
				this.link.close();
			this.servSocket.close();
		} catch (Exception e) {e.printStackTrace();}
	}

}
