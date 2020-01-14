
package model;
import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pseudo; 
	private InetAddress ip ; 
	
	public InetAddress getIp() {
		return ip;
	}


	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public User(String pseudo, InetAddress ip) {
		this.pseudo = pseudo;
		this.ip = ip; 
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	
	
	
}
