/**
 * 
 */
package org.bebrb.data;

import java.util.List;

/**
 * «апись с данными. «апись состоит из полей
 * @author Andrey Klyuev
 */
public interface Record {
	
	/**
	 * “ипы записи
	 * <ul>
	 * <li> {@link #Data} - обычные данные, полученные с сервера
	 * <li> {@link #Summary} - данные, которые добавлены на клиенте с целью подведени€ итогов по группам или в целом 
	 * <li> {@link #Buffer} - данные в буфере
	 * </ul>
	 * 
	 */
	public static enum Type{Data, Summary, Buffer};
	
	/**
	 * –ежим, в котором находитс€ запись
	 * <ul>
	 * <li> {@link #Browse} - обычный режим
	 * <li> {@link #Add} - режим добавлени€
	 * <li> {@link #Edit} - режим изменени€
	 * <li> {@link #Delete} - режим удалени€
	 * </ul>
	 * @see Record#getMode()
	 */
	public static enum Mode {Browse,Add,Edit,Delete};
	
	
	/**
	 * ¬озвращает режим, в котором находитс€ запись
	 * @return не может быть null
	 * @see Mode
	 */
	public Mode getMode();
	
	/**
	 * —писок полей в записи.  аждое поле содержит данные.
	 * @return не может быть null
	 */
	public List<Field<?>> getFields();
	
	/**
	 * Cписок значений в записи
	 * @return не может быть null
	 */
	public List<Object> getValues();
	
	/**
	 * ¬озвращает поле, которое €вл€етс€ ключом
	 * @return не может быть null. Ћюбой источник должен содержать ключ.
	 */
	public Field<?> getKey();
	
	/**
	 * ¬озвращает поле по его имени 
	 * @param fname им€ пол€
	 * @return не может быть null
	 * @exception Exception в случае если поле с именем fname не найдено 
	 * @see #findField(String)
	 */
	public Field<?> fieldByName(String fname) throws Exception;
	
	/**
	 * ¬озвращает поле по его имени 
	 * @param fname им€ пол€
	 * @return null в случае если поле с именем fname не найдено
	 * @see #fieldByName(String)
	 */
	public Field<?> findField(String fname);
	
	/**
	 * ¬озвращает пакет которому принадлежит запись
	 * @return может быть null, если запись находитс€ в режиме буфера
	 */
	public DataPage getDataPage();
	
	/**
	 * ‘иксируютс€ изменени€ в источнике данных. ¬озможна отправка на сервер
	 * @exception Exception в случае, если данные не прошли проверку
	 * 
	 */
	public void commit() throws Exception;
	
}
