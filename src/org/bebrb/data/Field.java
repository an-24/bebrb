/**
 * 
 */
package org.bebrb.data;

/**
 * Поле источника данных.
 * @author Andrey Klyuev
 *
 * @param <T> тип поля
 */
public interface Field<T> {
	/**
	 * Значение поля в записи
	 * @return может быть null для полей, которые имеют неопределенное значение
	 */
	public T getValue();
	
	/**
	 * Номер поля в записи
	 * @return целое число от 0 до кол-ва полей в записи, уменьшенное на единицу
	 */
	public int getIndex();
	
	/**
	 * Аттрибут соответствующий данному полю
	 * @return не может быть null
	 */
	public Attribute getAttribute();

}
