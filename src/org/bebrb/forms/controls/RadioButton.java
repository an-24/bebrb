/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.Control;

/**
 * Переключатель
 */
public interface RadioButton extends Control<Boolean> {
	/**
	 * Имя группировки. Все переключатели с одинаковой группировкой работают
	 * совместно. Группировка действует в рамках группирующего элемента, которой принадлежит
	 * переключатель.
	 * @return может быть null
	 */
	public String getGroup();
}
