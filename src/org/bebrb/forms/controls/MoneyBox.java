/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;
import org.bebrb.utils.Money;

/**
 * Поле ввода для денежной суммы
 *
 */
public interface MoneyBox extends ControlGroup<Money> {
	/**
	 * Метка для поля ввода
	 * @return не может быть null
	 */
	public Label getLabel();

	/**
	 * Ограничение ввода сверху
	 * @return если null ограничение отсутствует
	 */
	public Money getMaxValue();
	
	/**
	 * Ограничение ввода снизу
	 * @return если null ограничение отсутствует
	 */
	public Money getMinValue();
	
}
