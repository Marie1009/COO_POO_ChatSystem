package controller;

import java.net.Socket;

import model.User;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*Socket local = new Socket() ; 
		User dest= new User("sweety31",local) ; 
		Socket distant = new Socket() ; 
		User send= new User("wyl", distant) ; */
		
		Conversation listener= new Conversation(0) ;
		Conversation sender= new Conversation(1) ;
		sender.setMessage("coucou");
		
	}

}
