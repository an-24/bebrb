/**
 * 
 */
package org.bebrb.forms;

/**
 * Событие изменения состояния ввода
 */
public interface OnChange {
	/**
	 * Событие возникает после изменения состояния ввода
	 * @param control элемент управления, находящийся в фокусе ввода
	 */
	public void onChangeState(Control<?> control);
	/**
	 * Событие возникает после выполнения {@link Control#acceptInput()}
	 * @param control элемент управления, находящийся в фокусе ввода
	 */
	public void onAccept(Control<?> control);
	/**
	 * Событие возникает после выполнения {@link Control#cancelInput()}
	 * @param control элемент управления, находящийся в фокусе ввода
	 */
	public void onCancel(Control<?> control);
}
