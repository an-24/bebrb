/**
 * 
 */
package org.bebrb.context;

import java.util.logging.Logger;

/**
 * Контекст сессии
 * @author Andrey Klyuev
 *
 */
public interface SessionContext extends Context {

	/**
	 * Профиль активного пользователя, прошедшего процедуру аутентификации.
	 * @return профиль пользователя. 
	 */
	public UserContext getActiveUserContext();
	
	/**
	 * Закрытие сессии работы активного пользователя. После выполения {@link #getActiveUserContext()} будет 
	 * возвращать null.
	 */
	public void logout();
	
	/**
	 * Возвращает объект для логирования в рамках сессии
	 * @return не может быть null
	 */
	public Logger getLogger();
}
