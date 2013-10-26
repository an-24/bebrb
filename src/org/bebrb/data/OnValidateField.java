/**
 * 
 */
package org.bebrb.data;

import org.bebrb.forms.Control;

/**
 * Интерфейс контроля данных отдельного поля 
 * @author Andrey Klyuev
 */
public interface OnValidateField<T> {
	/**
	 * Вызывается для контроля вводимых данных. Вызывается методом {@link Control#acceptInput()}
	 * @param control элемент управления
	 * @param value данные, введенные пользователем 
	 * @return если ошибки нет возвращается null
	 */
	public InvalidData validate(Control<T> control, T value);

}
