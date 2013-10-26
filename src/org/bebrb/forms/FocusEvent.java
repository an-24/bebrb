/**
 * 
 */
package org.bebrb.forms;

/**
 * События фокуса ввода
 */
public interface FocusEvent {
	public void setOnFocus(OnChangeFocus onfocus);
	public void setOnBlur(OnChangeFocus onblur);

}
