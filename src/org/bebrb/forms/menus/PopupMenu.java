/**
 * 
 */
package org.bebrb.forms.menus;

import java.util.List;

import org.bebrb.forms.Control;

/**
 * Всплывающее меню
 */
public interface PopupMenu {
	public List<MenuItem> getItems();
	public void popup(int x,int y);
	public void popup(Control<?> control);
}
