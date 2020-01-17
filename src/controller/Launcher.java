package controller;

import database.DatabaseConnection;
import view.WelcomeFrame;

public class Launcher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub 

		DatabaseConnection.createNewDatabase("history.db");
		DatabaseConnection.createNewTableUsers();
		DatabaseConnection.createNewTableMessages();
		
		WelcomeFrame login = new WelcomeFrame();
	}

}
