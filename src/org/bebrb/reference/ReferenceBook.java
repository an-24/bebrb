/**
 * 
 */
package org.bebrb.reference;

import java.util.Map;

import org.bebrb.data.RemoteFunction;

/**
 * Справочник
 *
 */
public interface ReferenceBook {
	/**
	 * Тип, структура и определения справочника
	 * @return не может быть null
	 */
	public ReferenceBookMetaData getMetaData();
	/**
	 * Все доступные представления справочника
	 * @return поименованный список представлений. Не может быть null
	 */
	public Map<String, View> getViews();
	/**
	 * Представление справочника, действующее по умолчанию 
	 * @return представление. Не может быть null
	 */
	public View getDefaultView();
	
	/**
	 * Удаленная процедура для вставки данных
	 * @return может быть null
	 */
	public RemoteFunction getInsertFunc();
	/**
	 * Удаленная процедура для изменения(обновления) данных
	 * @return может быть null
	 */
	public RemoteFunction getUpdateFunc();
	/**
	 * Удаленная процедура для удаления данных
	 * @return может быть null
	 */
	public RemoteFunction getDeleteFunc();

}
