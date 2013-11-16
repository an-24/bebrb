package org.bebrb.data;

import java.util.List;

/**
 * Удаленная функция 
 *
 */
public interface RemoteFunction {
	
	/**
	 * Имя функции. Может содержать имя модуля, например <code>mod1.delAccount</code>. Для анонимных функций
	 * возвращается null.
	 * @return может быть null
	 */
	public String getName();
	
	/**
	 * Список именованных аргументов функции
	 * @return не может быть null
	 */
	public List<Argument> getArguments();
	
	/**
	 * Текст функции
	 * @return не может быть null
	 */
	public String getText();

}
