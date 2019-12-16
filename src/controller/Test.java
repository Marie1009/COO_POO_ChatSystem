package controller;

import java.net.Socket;

import model.BroadcastType;
import model.User;
import view.WelcomeFrame;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket local = new Socket() ; 
		User dest= new User("wylenolf",local) ; 
		Socket distant = new Socket() ; 
		User send= new User("wylenolf", distant) ;
		
		BroadcastListener listener = new BroadcastListener(dest) ;// attend
		/*try {
			Thread.sleep(2000);
		}catch(InterruptedException e) {e.printStackTrace();}*/
		
//		  BroadcastSender sender = new BroadcastSender(BroadcastType.NEW_CONNECTION);
//		  //envoi bd sender.setLocaluser(send);
//		  
//		  BroadcastSender sendr = new BroadcastSender(BroadcastType.NEW_CONNECTION);
//		  //envoi bd sendr.setLocaluser(lol); try { Thread.sleep(3000);
//		  }catch(Exception e) {}
//		  System.out.println("liste "+listener.getListOfConnected().get(0).getPseudo()+
//		  listener.getListOfConnected().get(1).getPseudo());
//		 
		

		/*Conversation listener = new Conversation(0) ;
		Conversation sender = new Conversation(1) ;

		sender.setMessage("bonjour Jeanne");*/

		/*Server s = new Server() ; 
		s.receive();*/
		WelcomeFrame testframe = new WelcomeFrame();
		
	
	}

}
