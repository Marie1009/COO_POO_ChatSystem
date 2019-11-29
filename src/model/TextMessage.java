package model;

public class TextMessage extends Message {
	private String text ; 
	
	public TextMessage(User dest, String text) {
		super(dest); 
		this.text=text; 
	}
	
	public String toString() {
		return text; 
	}
}
