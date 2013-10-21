/**
 * 
 */
package org.bebrb.context;

import org.bebrb.user.User;

/**
 * @author Andrey Klyuev
 *
 */
public interface UserContext extends Context {
	
	/**
	 * ¬озвращает пользовател€
	 * @return не может быть null
	 */
	public User getUser();

}
