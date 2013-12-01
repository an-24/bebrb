/**
 * 
 */
package org.bebrb.data;

import java.util.List;

/**
 * Пакет с данными. Источник данных состоит из пакетов, которые могут быть получены
 * раздельно и не сразу.
 * 
 * @author Andrey Klyuev
 *
 */
public interface DataPage {
	
	/**
	 * Возвращает кол-во записей, полученные пакетом.   
	 * @return кол-во записей в пакете. Если пакет не {@link #isAlive()} то возвращается ноль 
	 */
	public int getSize();
	
	/**
	 * Список записей, полученных пакетом. Если
	 * записи еще не получены, то осуществляется попытка их получения  
	 * @return не может быть null
	 */
	public List<Record> getRecords() throws Exception;
	
	/**
	 * Тест на последний пакет в источнике данных
	 * @return true если пакет последний, иначе false
	 * @throws Exception возникает, если пакет еще не получил данные
	 */
	public boolean isEof() throws Exception;
	
	/**
	 * Тест на то, что пакет получил все записи
	 * @return true когда пакет заполнен, иначе false 
	 */
	public boolean isAlive();
	

}
