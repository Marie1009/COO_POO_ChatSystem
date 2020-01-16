package controller;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import database.DatabaseConnection;
import model.Message;
import model.User;
import view.ChatFrame;
import view.ConversationFrame;
import view.WelcomeFrame;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub 

		DatabaseConnection.createNewDatabase("history.db");

		DatabaseConnection.createNewTableUsers();
		DatabaseConnection.createNewTableMessages();
		DatabaseConnection.createNewTableSelf();
		//lance un faux utilisateur dont le pseudo est wyleno qui permet de tester les requêtes pseudo unique et connected users
		//BroadcastListener fakeuser = new BroadcastListener(u1) ;

		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DatabaseConnection.selectAllUsers();
		DatabaseConnection.selectAllMessages();
		
		try {
			String previous = DatabaseConnection.selectUser(InetAddress.getByName("10.1.5.44")) ;
			System.out.println(previous);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//lance la fenêtre de login qui permet d'accéder aux conversations qaund le pseudo fournit est adéquat (unique et > 5 caractères
		WelcomeFrame login = new WelcomeFrame();
		//ChatFrame cf = new ChatFrame("pseudo") ; 
		//DatabaseConnection.insert(u1, m);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(DatabaseConnection.selectIp("tata")) ;
		//DatabaseConnection.select(u1);
	}

}
