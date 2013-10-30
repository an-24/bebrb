/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;

/**
 * Панель
 */
public interface Panel extends ControlGroup<Void> {
	/**
	 * Заголовок панели
	 * @return может быть null
	 */
	public String getCaption();

}
