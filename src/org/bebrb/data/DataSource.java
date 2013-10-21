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
public interface DataSource {
	
	/**
	 * ”никальный идентификатор источника
	 * @return
	 */
	public String getId();
	
	/**
	 * “ест на получение данных в момент обращени€ к ним
	 * @return если false тогда данные получаютс€ сразу при вызове {@link #getDataPages()}, иначе
	 * данные получаютс€ постранично при вызове  {@link DataPage#getRecords()}
	 */
	public boolean isLazy();
	
	/**
	 * ¬озвращает список атрибутов источника данных
	 * @return не может быть null
	 */
	public List<Attribute> getAttributes() throws Exception;
	
	/**
	 * —писок страниц
	 * @return  не может быть null
	 * @throws Exception
	 */
	public List<DataPage> getDataPages() throws Exception;

}
