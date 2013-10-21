/**
 * 
 */
package org.bebrb.data;

import java.util.List;

/**
 * 
 * @author Andrey Klyuev
 *
 */
public interface DataPage {
	/**
	 * @return максимальный размер пакета
	 */
	public int getMaxSize();
	
	/**
	 * Возвращает кол-во записей, полученных пакетом. Если
	 * записи еще не получены, то осуществляется попытка их получения  
	 * @return размер записей в пакете
	 */
	public int getSize()  throws Exception;
	
	/**
	 * Список записей, полученных пакетом. Если
	 * записи еще не получены, то осуществляется попытка их получения  
	 * @return не может быть null
	 */
	public List<Record> getRecords() throws Exception;

}
