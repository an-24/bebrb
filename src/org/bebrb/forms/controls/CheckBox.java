package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;

/**
 * Флажок опции 
 *
 */
public interface CheckBox extends ControlGroup<Boolean> {
	/**
	 * Метка для флажка
	 * @return не может быть null
	 */
	public Label getLabel();
}
