package org.bebrb.data;

/**
 * Аргумент для удаленной функции
 * @see RemoteFunction
 *  
 */
public interface Argument {
	/**
	 * Имя аргумента
	 * @return  не может быть null
	 */
	public String getName();

	/**
	 * Тип аргумента
	 * @return  не может быть null
	 */
	public Attribute.Type getType();
}
