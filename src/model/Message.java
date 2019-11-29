package model;

public abstract class Message {
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
