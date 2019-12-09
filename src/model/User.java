

import java.net.Socket;

public class User {
	private String pseudo; 
	private Socket sckt ;
	
	public User(String pseudo, Socket sckt) {
		super();
		this.pseudo = pseudo;
		this.sckt = sckt;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public Socket getSckt() {
		return sckt;
	}

	public void setSckt(Socket sckt) {
		this.sckt = sckt;
	} 
	
	
}
