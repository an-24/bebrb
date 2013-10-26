/**
 * 
 */
package org.bebrb.context;

/**
 * 
 * @author Andrey Klyuev
 *
 */
public interface Context {
	
	/**
	 * Возвращает объект с конфигурацией
	 * @return не может быть null
	 */
	public Config getConfig();

}
