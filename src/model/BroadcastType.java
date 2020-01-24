package model;

/** Types of UDP broadcast. Used by BroadcastSender(). 
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public enum BroadcastType {
	PSEUDO_UNIQUE,
	NEW_CONNECTION,
	USER_LEAVING,
}
