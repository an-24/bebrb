/**
 * 
 */
package org.bebrb.forms;

import org.bebrb.utils.ShiftState;

/**
 * Событие от клавиатуры
 */
public interface OnKeyboard {
	/**
	 * Событие возникает при нажатии клавиши
	 * @param control элемент управления, находящийся в фокусе ввода
	 * @param key код клавиши
	 * @param state состояние клавиш-модификаторов
	 */
	public void onKeyDown(Control<?> control, int key, ShiftState state);
	/**
	 * Событие возникает при отжатии клавиши
	 * @param control элемент управления, находящийся в фокусе ввода
	 * @param key код клавиши
	 * @param state состояние клавиш-модификаторов
	 */
	public void onKeyUp(Control<?> control, int key, ShiftState state);
	
	/**
	 * Событие возникает при вводе символа
	 * @param control элемент управления, находящийся в фокусе ввода
	 * @param key введеный символ
	 */
	public void onChar(Control<?> control, char key);
}
