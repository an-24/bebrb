/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;

/**
 * Поле ввода для чисел с плавающей точкой
 *
 */
public interface DoubleBox extends ControlGroup<Double> {
	/**
	 * Метка для поля ввода
	 * @return не может быть null
	 */
	public Label getLabel();

	/**
	 * Ограничение ввода сверху
	 * @return если null ограничение отсутствует
	 */
	public Double getMaxValue();
	
	/**
	 * Ограничение ввода снизу
	 * @return если null ограничение отсутствует
	 */
	public Double getMinValue();
}
