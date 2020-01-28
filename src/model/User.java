
package model;
import java.io.Serializable;
import java.net.InetAddress;

/** Contains a pseudo and an IP address.
 * 
 * @author Jeanne Bertrand and Marie Laur 
 *
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String pseudo; 
	private InetAddress ip ; 
	
	/** Constructor. 
	 * 
	 * @param pseudo 
	 * @param ip address
	 */
	public User(String pseudo, InetAddress ip) {
		this.pseudo = pseudo;
		this.ip = ip; 
	}

	/** Returns the IP address.
	 * 
	 * @return the ip address
	 */
	public InetAddress getIp() {
		return ip;
	}

	/** Sets the IP address.
	 * 
	 * @param ip the ip address
	 */
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	
	/** Returns the pseudo.
	 * 
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/** Sets the pseudo. 
	 * 
	 * @param pseudo
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

}
