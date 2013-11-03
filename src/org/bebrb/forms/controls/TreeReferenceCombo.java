/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.reference.ReferenceBookMetaData.ReferenceType;

/**
 * Элемент выбора из справочника с типом {@link ReferenceType#Hierarchy}
 */
public interface TreeReferenceCombo extends LinearReferenceCombo {
	/**
	 * Возвращает текущую папку
	 * @return не может быть null
	 */
	public Integer getCurrentFolder();
	
	/**
	 * Установить текущую папку
	 * @param key идентификатор папки
	 */
	public void setCurrentFolder(Integer key);
	
	/**
	 * Перейти к корневой папке текущего представления
	 */
	public void jumpToRoot();

}
