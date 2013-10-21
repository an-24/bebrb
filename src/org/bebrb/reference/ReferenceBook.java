/**
 * 
 */
package org.bebrb.reference;

import java.util.Map;

/**
 * —правочник
 * @author Andrey Klyuev
 *
 */
public interface ReferenceBook {
	
	/**
	 * ”никальный идентификатор справочника
	 * @return  не может быть null
	 */
	public String getReferenceId();
	
	/**
	 * “ип, структура и определени€ справочника
	 * @return не может быть null
	 */
	public ReferenceBookMetaData getMetaData();
	/**
	 * ¬се доступные представлени€ справочника
	 * @return поименованный список представлений. Ќе может быть null
	 */
	public Map<String, View> getViews();
	/**
	 * ѕредставление справочника, действующее по умолчанию 
	 * @return представление. Ќе может быть null
	 */
	public View getDefaultView();

}
