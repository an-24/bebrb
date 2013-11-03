/**
 * 
 */
package org.bebrb.reference;

import org.bebrb.data.DataSource;

/**
 * Представление справочника
 */
public interface View {
	/**
	 * Уникальное в рамках справочника представление
	 * @return
	 */
	public String getName();
	/**
	 * Заголовок представления
	 */
	public String getTitle();
	/**
	 * Возвращает данные представления справочника. При первом обращении за сеанс
	 * проводится проверка кеша справочника. Если он устарел происходит вызов {@link #refresh()}
	 * @return не может быть null
	 * @throws Exception
	 */
	public DataSource getDataSet() throws Exception;
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
}
