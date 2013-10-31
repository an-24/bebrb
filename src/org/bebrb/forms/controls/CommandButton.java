/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.Control;
import org.bebrb.forms.menus.PopupMenu;

/**
 * Кнопка с выпадающим списком команд
 */
public interface CommandButton extends Control<Void> {
	/**
	 * Надпись на кнопке
	 * @return не может быть null
	 */
	public String getCaption();
	/**
	 * Список выпадающих при нажатии на кнопку команд
	 * @return не может быть null
	 */
	public PopupMenu getCommandMenu();

}
