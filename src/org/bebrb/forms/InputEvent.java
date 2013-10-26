/**
 * 
 */
package org.bebrb.forms;

/**
 * События ввода данных
 */
public interface InputEvent {
	public void setOnChange(OnChange onchange);
	public void setOnKeyboard(OnKeyboard onkeyboard);
}
