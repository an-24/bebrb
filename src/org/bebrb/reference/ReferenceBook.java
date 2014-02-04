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
	public static final int MAIN_ROOT_ID = -1;
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
	 * Тест на возможность добавлять записи 
	 * @return true если в источник можно добавлять новые записи
	 */
	public boolean isCanAdd();

	/**
	 * Тест на возможность удаления записей 
	 * @return true если в источнике можно удалять записи
	 */
	public boolean isCanDelete();
	
	/**
	 * Тест на возможность изменять записи 
	 * @return true если в источник можно изменять записи
	 */
	public boolean isCanEdit();
	

}
