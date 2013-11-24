/**
 * 
 */
package org.bebrb.context;

import java.util.logging.Logger;

/**
 * Session context
 *
 */
public interface SessionContext extends Context {
	
	/**
	 * Unique session ID number
	 * @return can not be null
	 */
	public String getId();  

	/**
	 * Profile of the active user authenticated.
	 * @return can not be null 
	 */
	public UserContext getActiveUserContext();
	
	/**
	 * Close session of the active user. After call {@link #getActiveUserContext()} will return null.
	 */
	public void logout();
	
	/**
	 * Return object for logging in the session
	 * @return  can not be null
	 */
	public Logger getLogger();
}
