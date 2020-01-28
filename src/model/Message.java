package model;

import java.io.Serializable;

/** Object used for TCP communication, in the ChatFrame. 
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class Message implements Serializable { 

	private static final long serialVersionUID = 1L;
	private User dest; 
	private User src ; 
	private String content ; 

	/** Constructor. 
	 * 
	 * @param content the message text
	 * @param dest the receiving user
	 * @param src the sending user
	 */
	public Message(String content, User dest, User src) {
		this.src = src ; 
		this.dest = dest; 
		this.content= content ; 
	}
	
	/** Returns the source User
	 * 
	 * @return the source User
	 */
	public User getSrc() {
		return src;
	}

	/** Sets the source User
	 * 
	 * @param src the source User
	 */
	public void setSrc(User src) {
		this.src = src;
	}

	/** Returns the destination User 
	 * 
	 * @return the destination User
	 */
	public User getDest() {
		return dest;
	}

	/** Sets the destination User
	 * 
	 * @param dest the destination User 
	 */
	public void setDest(User dest) {
		this.dest = dest;
	}
	
	/** Returns the message text or content
	 * 
	 * @return the content
	 */
	public String getContent( ) {
		return this.content ; 
	}
	
	/** Sets the message text or content
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content ; 
	}
}
