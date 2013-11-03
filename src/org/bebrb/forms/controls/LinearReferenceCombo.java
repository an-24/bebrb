/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData.ReferenceType;
import org.bebrb.reference.View;

/**
 * Элемент выбора из справочника с типом {@link ReferenceType#Linear}
 */
public interface LinearReferenceCombo  extends SimpleReferenceCombo {
	/**
	 * Возвращает текущее представление
	 * @return не может быть null
	 */
	public View getCurrentView();
	
	/**
	 * Устанавливает текущее представление
	 * @param view не может быть null
	 */
	public void setCurrentView(View view);
	
	/**
	 * Устанавливает текущим представление по умолчанию
	 * @see ReferenceBook#getDefaultView() 
	 */
	public void setCurrentView();
	
}
