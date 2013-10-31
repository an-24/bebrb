/**
 * 
 */
package org.bebrb.forms.menus;

/**
 * Пункт меню - флажок или переключатель
 */
public interface MenuCheckItem extends MenuAction {
	/**
	 * Состояние флажка/переключателя
	 * @return true означает выбран
	 */
	public boolean isChecked();
	/**
	 * Имя группировки. Все переключатели с одинаковой группировкой работают
	 * совместно. Группировка действует в рамках одного меню
	 * @return может быть null. В этом случае меню является флажком
	 */
	public String getGroup();
}
