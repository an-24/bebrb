/**
 * 
 */
package org.bebrb.context;

/**
 * @author Andrey Klyuev
 *
 */
public interface Config {
	
	/**
	 * Возвращает значения параметра с определенным именем. Если параметра нет, возвращает
	 * значение по умолчанию  
	 * @param name имя параметра
	 * @param def значение по умолчанию
	 * @return значение параметра или значение def 
	 */
	public String getParam(String name, String def);

	/**
	 * Устанавливает параметру с именем name значение value 
	 * @param name имя параметра
	 * @param value новое значение параметра
	 */
	public void setParam(String name, String value);
	
	
}
