/**
 * 
 */
package org.bebrb.context;

import org.bebrb.user.User;

/**
 *
 */
public interface UserContext extends Context {
	
	/**
	 * Return user info
	 * @return can not be null
	 */
	public User getUser();
	/**
	 * Return about user agent info
	 * @return may be null
	 */
	public UserAgent getAgentInfo();

}
