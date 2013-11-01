package org.bebrb.data;

/**
 * Представляет атрибут в источнике данных 
 *
 */
public interface Attribute {
	/**
	 * Типы атрибутов 
	 * <ul>
	 * <li> {@link #Integer} - целое число (32 разряда)
	 * <li> {@link #String} - строка. Маскимальное кол-во символов в {@link Attribute#getMaxSizeChar()} 
	 * <li> {@link #Money} - число с фиксированной точностью в два знака после запятой
	 * <li> {@link #Double} - число с плавающей точкой (32 разряда)
	 * <li> {@link #Date} - дата/время
	 * <li> {@link #Image} - вариант бинарного объекта {@link #Blob}, который может быть интерпретирован как изображение
	 * <li> {@link #Text} - вариант бинарного объекта {@link #Blob}, который может быть интерпретирован как текст
	 * <li> {@link #Blob} - бинарный объект
	 * <li> {@link #Boolean} - булево значение
	 * </ul>
	 */
	public static enum Type{Integer, String, Money, Double, Date, Image, Text, Blob, Boolean};
	
	
	/**
	 * Источник данных в котором используется данный атрибут
	 * @return  не может быть null
	 */
	public DataSource getDataSource();
	
	/**
	 * Имя атрибута
	 * @return  не может быть null
	 */
	public String getName();
	
	/**
	 * Имя атрибута для пользователя
	 * @return не может быть null. Когда явно не назначен равно {@link #getName()}
	 */
	public String getCaption();
	
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
	
	/**
	 * Возвращает максимальное кол-во символов, которые можно разместить в атрибуте. Имеет смысл для атрибута типа {@link #String}.
	 * Для остальных типов возвращает 0. 
	 * @return размер данных в символах
	 */
	public int getMaxSizeChar();
	
}