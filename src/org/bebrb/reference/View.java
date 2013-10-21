/**
 * 
 */
package org.bebrb.reference;

import org.bebrb.data.DataSource;

/**
 * Представление справочника
 * @author Andrey Klyuev
 *
 */
public interface View {
	
	/**
	 * Уникальное в рамках справочника представление
	 * @return
	 */
	public String getName();
	
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
}
