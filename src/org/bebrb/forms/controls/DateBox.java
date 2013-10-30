/**
 * 
 */
package org.bebrb.forms.controls;

import java.util.Date;

import org.bebrb.forms.ControlGroup;

/**
 * 
 * Поле ввода для даты
 *
 */
public interface DateBox extends ControlGroup<Date> {
	/**
	 * Метка для поля ввода
	 * @return не может быть null
	 */
	public Label getLabel();

}
