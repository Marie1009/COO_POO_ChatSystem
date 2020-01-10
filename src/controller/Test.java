package controller;

import java.net.Socket;

import database.DatabaseConnection;
import model.Message;
import model.User;
import view.WelcomeFrame;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub 
		User u1= new User("toto", new Socket()) ;
		Message m = new Message("ldfgslixdqdddddddddddddddddddddddddddlol") ; 
		
		DatabaseConnection.createNewDatabase("history.db");

		DatabaseConnection.createNewTableUsers();
		DatabaseConnection.createNewTableMessages();

		//lance un faux utilisateur dont le pseudo est wyleno qui permet de tester les requêtes pseudo unique et connected users
		//BroadcastListener fakeuser = new BroadcastListener(u1) ;
		
		//lance la fenêtre de login qui permet d'accéder aux conversations qaund le pseudo fournit est adéquat (unique et > 5 caractères
		WelcomeFrame login = new WelcomeFrame();
		//DatabaseConnection.insert(u1, m);
		//DatabaseConnection.selectAll();
		//DatabaseConnection.select(u1);
	}

}
