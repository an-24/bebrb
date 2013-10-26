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
	 * Возвращает пользователя
	 * @return не может быть null
	 */
	public User getUser();

}
