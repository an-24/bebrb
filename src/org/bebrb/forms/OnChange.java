/**
 * 
 */
package org.bebrb.forms;

/**
 * Событие изменения состояния ввода
 */
public interface OnChange {
	public void onChange(Control<?> control);
	public void onAccept(Control<?> control);
	public void onCancel(Control<?> control);
}
