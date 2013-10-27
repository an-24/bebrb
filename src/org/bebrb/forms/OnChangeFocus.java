/**
 * 
 */
package org.bebrb.forms;

/**
 * Событие изменения фокуса
 *
 */
public interface OnChangeFocus {
	/**
	 * Событие возникает после изменения фокуса ввода
	 * @param control элемент управления, который поменял фокус ввода
	 */
	public void onChange(Control<?> control);
}
