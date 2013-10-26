/**
 * 
 */
package org.bebrb.data;

import java.io.InputStream;
import java.io.OutputStream;

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
	 * Читает значение поля, которое является потоковым. Используется, в основном, для чтения полей из типов
	 * {@link Attribute.Type#Blob},{@link Attribute.Type#Image} или {@link Attribute.Type#Text}. Поля других типов
	 * также могут читать себя из потока. 
	 * @param out не может быть null
	 */
	public void getValue(OutputStream out);
	
	/**
	 * Установить значение поля
	 * @param value может быть null 
	 */
	public void setValue(T value);
	/**
	 * Установить значение которое является потоковым. Используется, в основном, для записи полей из типов
	 * {@link Attribute.Type#Blob},{@link Attribute.Type#Image} или {@link Attribute.Type#Text}. Поля других типов
	 * также могут писать себя в поток. 
	 * @param input может быть null
	 */
	public void setValue(InputStream input);
	
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
	
	
	/**
	 * Запись в которой располагается данной поле
	 * @return не может быть null
	 */
	public Record getRecord();
	

}
