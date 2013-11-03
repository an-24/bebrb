/**
 * 
 */
package org.bebrb.reference;

import java.util.Map;

/**
 * Справочник
 *
 */
public interface ReferenceBook {
	
	/**
	 * Уникальный идентификатор справочника
	 * @return  не может быть null
	 */
	public String getReferenceId();
	
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

}
