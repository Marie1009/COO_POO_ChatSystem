package model;

import java.io.Serializable;

public abstract class Message implements Serializable { //conseil de ruben et matthieu : implémenter Serializable : permet de mettre un peu n'importe quoi dans un objet et de l'envoyer
	private User dest; 
	
	public Message(User dest) {
		this.dest= dest ; 
	}
	
	public void send() {
		System.out.println("Sending your message...");
	}
	
	public String toString() {
		return " "; 
	}
}
