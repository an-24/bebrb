/**
 * 
 */
package org.bebrb.forms;

/**
 * Событие шелчка мышью
 *
 */
public interface OnClick {
	/**
	 * Событие возникает после шелчка мышью на элементе управления 
	 * @param control элемент управления-источник события
	 */
	public void onClick(Control<?> control);
}
