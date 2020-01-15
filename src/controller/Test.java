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

		//lance un faux utilisateur dont le pseudo est wyleno qui permet de tester les requêtes pseudo unique et connected users
		//BroadcastListener fakeuser = new BroadcastListener(u1) ;
		/*try {
			ConversationFrame conv = new ConversationFrame(new User("titi",InetAddress.getLocalHost()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//lance la fenêtre de login qui permet d'accéder aux conversations qaund le pseudo fournit est adéquat (unique et > 5 caractères
		WelcomeFrame login = new WelcomeFrame();
		//ChatFrame cf = new ChatFrame("pseudo") ; 
		//DatabaseConnection.insert(u1, m);
		//DatabaseConnection.selectAllMessages();
		//DatabaseConnection.select(u1);
	}

}
