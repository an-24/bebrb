/**
 * 
 */
package org.bebrb.forms;

import org.bebrb.data.OnValidateField;

/**
 * События проверки ввода
 *
 */
public interface ValidateEvent<T> {
	/**
	 * Устанавливает объект контроля ввода данных.  
	 * @param value может быть null. В этом сучае контроль снимается
	 */
	public void setValidator(OnValidateField<T> value);

}
