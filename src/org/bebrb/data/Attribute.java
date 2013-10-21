package org.bebrb.data;

/**
 * Представляет атрибут в источнике данных 
 *
 */
public interface Attribute {
	/**
	 * Типы атрибутов в справочнике 
	 */
	public static enum Type{Integer, String, Currency, Double, Date, Image, Text, Blob};
	
	
	/**
	 * Источник данных в котором используется данный атрибут
	 * @return  не может быть null
	 */
	public String getDataSourceId();
	
	/**
	 * Имя атрибута
	 * @return  не может быть null
	 */
	public String getName();
	/**
	 * Тип атрибута
	 * @return  не может быть null
	 */
	public Attribute.Type getType();
	/**
	 * Тест на ключ
	 * @return true если атрибут является ключем
	 */
	public boolean isKey();
	
	/**
	 * Тест на внешний ключ
	 * @return возвращает атрибут на который ссылается данный атрибут. Может быть null, если атрибут не является внешним ключем
	 */
	public Attribute getForeignKey();
	
	/**
	 * Видимость атрибута.
	 * @return true если атрибут видим
	 */
	public boolean isVisible();
	
	/**
	 * Обязательность атрибута.
	 * @return true если атрибут обязателен
	 */
	public boolean isMandatory();
}