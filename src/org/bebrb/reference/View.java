/**
 * 
 */
package org.bebrb.reference;

import java.util.Map;

import org.bebrb.data.DataPage;
import org.bebrb.data.DataSource;

/**
 * Reference view
 */
public interface View {
	/**
	 * Unique name view in reference
	 * @return is not null
	 */
	public String getName();
	/**
	 * Title view
	 * @return is not null
	 */
	public String getTitle();
	/**
	 * Возвращает данные представления справочника. При первом обращении за сеанс
	 * проводится проверка кеша справочника. Если он устарел происходит вызов {@link #refresh()}
	 * @return не может быть null
	 * @throws Exception
	 */
	public DataSource getDatasource() throws Exception;
	/**
	 * Обновление данных в кеше справочника. Приводит к сетевому соединению и обмену
	 * @throws Exception
	 */
	public void refresh() throws Exception;
	
	/**
	 * Значение ключа {@link ReferenceBookMetaData#getKey()} корневой 
	 * папки представления. Имеет смысл для типа справочника {@link ReferenceBookMetaData.ReferenceType#Hierarchy}
	 * @return null для не иерархических справочников. Для справочников {@link ReferenceBookMetaData.ReferenceType#Hierarchy}
	 * никогда не null
	 */
	public Integer getRoot();
	
	/**
	 * Тест на получение данных в момент обращения к ним
	 * @return если false тогда данные получаются сразу при открытии, иначе
	 * данные получаются постранично при вызове  {@link DataPage#getRecords()}
	 */
	public boolean isLazy();
}
