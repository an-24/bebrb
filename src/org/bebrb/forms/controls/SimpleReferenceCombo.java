/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.data.Record;
import org.bebrb.forms.ControlGroup;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData.ReferenceType;

/**
 * Элемент выбора из справочника с типом {@link ReferenceType#Simple}
 */
public interface SimpleReferenceCombo extends ControlGroup<Integer> {
	/**
	 * Метка для поля ввода
	 * @return не может быть null
	 */
	public Label getLabel();
	/**
	 * Ссылка на справочник
	 * @return может быть null
	 */
	public ReferenceBook getReferenceBook();
	/**
	 * Выбранная запись
	 * @return может быть null
	 */
	public Record getSelectedRecord();

}
