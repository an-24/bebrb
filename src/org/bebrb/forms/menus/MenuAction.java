package org.bebrb.forms.menus;

import org.bebrb.forms.Action;
import org.bebrb.forms.controls.ImageBox;
import org.bebrb.utils.KeyCode;

/**
 * Пункт меню, связанное с действием 
 *
 */
public interface MenuAction extends MenuItem {
	/**
	 * Горячая комбинация клавиш
	 * @return может быть null
	 */
	public KeyCode getShortKey();
	/**
	 * Картинка пункта меню
	 * @return может быть null
	 */
	public ImageBox getImage();
	/**
	 * Действие
	 * @return может быть null
	 */
	public Action getAction();

}
