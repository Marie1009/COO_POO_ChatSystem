package model;

import java.io.Serializable;

//mettre l'enumération dans la classe direct. on peut alors tester direct le type au lieu de faire regex dégueu
public class Message implements Serializable { //conseil de ruben et matthieu : implémenter Serializable : permet de mettre un peu n'importe quoi dans un objet et de l'envoyer
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User dest; 
	private User src ; 
	
	public User getSrc() {
		return src;
	}

	public void setSrc(User src) {
		this.src = src;
	}

	public User getDest() {
		return dest;
	}

	public void setDest(User dest) {
		this.dest = dest;
	}

	private String content ; 
	
	public Message(String content, User dest, User src) {
		this.src = src ; 
		this.dest = dest; 
		this.content= content ; 
	}
	
	public void send() {
		System.out.println("Sending your message...");
	}
	
	public String toString() {
		return " "; 
	}
	
	public String getContent( ) {
		return this.content ; 
	}
	
	public void setContent(String content) {
		this.content = content ; 
	}
}
