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
		
		/*Conversation sender= new Conversation(1) ;
		
		Conversation listener= new Conversation(0) ;
		
		sender.setMessage("coucou");*/
		Client c = new Client() ; 
		Server s = new Server() ; 
		s.receive();
		c.send(); 
		
	}

}
