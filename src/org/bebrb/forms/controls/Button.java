/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.Control;

/**
 * Кнопка команды
 */
public interface Button extends Control<Void> {
	/**
	 * Надпись на кнопке
	 * @return не может быть null
	 */
	public String getCaption();

}
