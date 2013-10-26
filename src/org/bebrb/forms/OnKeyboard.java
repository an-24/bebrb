/**
 * 
 */
package org.bebrb.forms;

import org.bebrb.utils.ShiftState;

/**
 * Событие от клавиатуры
 */
public interface OnKeyboard {
	public void onKeyDown(Control<?> control, int key, ShiftState state);
	public void onKeyUp(Control<?> control, int key, ShiftState state);
	public void onChar(Control<?> control, char key);
}
