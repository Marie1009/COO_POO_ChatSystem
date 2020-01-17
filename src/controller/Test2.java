package controller;

import java.net.Socket;

import database.DatabaseConnection;
import model.User;
import view.WelcomeFrame;

public class Test2 {

	/* à lancer sur un autre pc en ssh via le jar wylenolfuser.jar situé dans le fichier racine du projet
	 * permet de tester la communication des broadcast entre différents PC
	 * java -jar wylenolfuser.jar
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//User lolz= new User("wylenolf", new Socket()) ;
		DatabaseConnection.createNewDatabase("history.db");

		DatabaseConnection.createNewTableUsers();
		DatabaseConnection.createNewTableMessages();

		DatabaseConnection.selectAllMessages();
		DatabaseConnection.selectAllUsers();
		WelcomeFrame wf = new WelcomeFrame() ; 
		//lance un faux utilisateur dont le pseudo est wylenolf qui permet de tester les requêtes pseudo unique et connected users
		//BroadcastListener fu = new BroadcastListener(lolz) ;// attend
	}

}
