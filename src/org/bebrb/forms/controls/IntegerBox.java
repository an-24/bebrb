/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;

/**
 * Поле ввода для целых чисел
 */
public interface IntegerBox extends ControlGroup<Integer> {
	/**
	 * Метка для поля ввода
	 * @return не может быть null
	 */
	public Label getLabel();
	
	/**
	 * Ограничение ввода сверху
	 * @return если null ограничение отсутствует
	 */
	public Integer getMaxValue();
	
	/**
	 * Ограничение ввода снизу
	 * @return если null ограничение отсутствует
	 */
	public Integer getMinValue();
}
