/**
 * 
 */
package org.bebrb.data;

/**
 * ѕоле источника данных.
 * @author Andrey Klyuev
 *
 * @param <T> тип пол€
 */
public interface Field<T> {
	/**
	 * «начение пол€ в записи
	 * @return может быть null дл€ полей, которые имеют неопределенное значение
	 */
	public T getValue();
	
	/**
	 * Ќомер пол€ в записи
	 * @return целое число от 0 до кол-ва полей в записи, уменьшенное на единицу
	 */
	public int getIndex();
	
	/**
	 * јттрибут соответствующий данному полю
	 * @return не может быть null
	 */
	public Attribute getAttribute();

}
