/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;

/**
 * Переключатель
 */
public interface RadioButton extends ControlGroup<Boolean> {
	/**
	 * Имя группировки. Все переключатели с одинаковой группировкой работают
	 * совместно. Группировка действует в рамках группирующего элемента, которой принадлежит
	 * переключатель.
	 * @return может быть null
	 */
	public String getGroup();
	/**
	 * Метка для переключателя
	 * @return не может быть null
	 */
	public Label getLabel();
	
}
