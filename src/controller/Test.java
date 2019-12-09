package controller;

import java.net.Socket;

import model.User;
import view.WelcomeFrame;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket local = new Socket() ; 
		User dest= new User("wyl",local) ; 
		Socket distant = new Socket() ; 
		User send= new User("wyl", distant) ;
		
		ServerBroadcast sb2 = new ServerBroadcast(send) ;// attend
		/*try {
			Thread.sleep(2000);
		}catch(InterruptedException e) {e.printStackTrace();}*/
		ServerBroadcast sb1 = new ServerBroadcast(dest,0); //envoi bd
		
		

		/*Conversation listener = new Conversation(0) ;
		Conversation sender = new Conversation(1) ;

		sender.setMessage("bonjour Jeanne");*/

		/*Server s = new Server() ; 
		s.receive();*/
		WelcomeFrame testframe = new WelcomeFrame();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				testframe.createAndShowGUI();
			}
		});
	}

}
