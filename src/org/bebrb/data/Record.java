/**
 * 
 */
package org.bebrb.data;

import java.util.List;

/**
 * Запись с данными. Запись состоит из полей
 * @author Andrey Klyuev
 */
public interface Record {
	
	/**
	 * Типы записи
	 * <ul>
	 * <li> {@link #Data} - обычные данные, полученные с сервера
	 * <li> {@link #Summary} - данные, которые добавлены на клиенте с целью подведения итогов по группам или в целом 
	 * <li> {@link #Buffer} - данные в буфере
	 * </ul>
	 * 
	 */
	public static enum Type{Data, Summary, Buffer};
	
	/**
	 * Режим, в котором находится запись
	 * <ul>
	 * <li> {@link #Browse} - обычный режим
	 * <li> {@link #Add} - режим добавления
	 * <li> {@link #Edit} - режим изменения
	 * <li> {@link #Delete} - режим удаления
	 * </ul>
	 * @see Record#getMode()
	 */
	public static enum Mode {Browse,Add,Edit,Delete};
	
	
	/**
	 * Возвращает режим, в котором находится запись
	 * @return не может быть null
	 * @see Mode
	 */
	public Mode getMode();
	
	/**
	 * Список полей в записи. Каждое поле содержит данные.
	 * @return не может быть null
	 */
	public List<Field<?>> getFields();
	
	/**
	 * Cписок значений в записи
	 * @return не может быть null
	 */
	public List<Object> getValues();
	
	/**
	 * Возвращает поле, которое является ключом
	 * @return не может быть null. Любой источник должен содержать ключ.
	 */
	public Field<?> getKey();
	
	/**
	 * Возвращает поле по его имени 
	 * @param fname имя поля
	 * @return не может быть null
	 * @exception Exception в случае если поле с именем fname не найдено 
	 * @see #findField(String)
	 */
	public Field<?> fieldByName(String fname) throws Exception;
	
	/**
	 * Возвращает поле по его имени 
	 * @param fname имя поля
	 * @return null в случае если поле с именем fname не найдено
	 * @see #fieldByName(String)
	 */
	public Field<?> findField(String fname);
	
	/**
	 * Возвращает пакет которому принадлежит запись
	 * @return может быть null, если запись находится в режиме {@link Mode#Add}
	 */
	public DataPage getDataPage();
	
	/**
	 * Фиксируются изменения в источнике данных. Возможна отправка на сервер
	 * @exception Exception в случае, если данные не прошли проверку
	 * 
	 */
	public void commit() throws Exception;
	
}
