package controller;

import database.DatabaseConnection;
import view.WelcomeFrame;

/** Main class for ChatSystem project.
 * Initializes a database with all needed tables in ./db directory and launches first frame.
 * Will fail if directory doesn't exists. 
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class Launcher {

	public static void main(String[] args) {
		DatabaseConnection.connect();
		DatabaseConnection.createNewTableUsers();
		DatabaseConnection.createNewTableMessages();
		
		@SuppressWarnings("unused")
		WelcomeFrame login = new WelcomeFrame();
	}

}
