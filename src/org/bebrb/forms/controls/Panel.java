/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;
import org.bebrb.utils.Border;

/**
 * Панель
 */
public interface Panel extends ControlGroup<Void> {
	/**
	 * Заголовок панели
	 * @return может быть null
	 */
	public String getCaption();
	/**
	 * Границы панели 
	 * @param type определяет местоположение границы
	 * @return не может быть null
	 */
	public Border getBorders(Border.Type type);
	/**
	 * Устанавливает видимость рамок вокруг панели
	 * @param value видимость
	 */
	public void setBorderARoundVisible(boolean value);

}
