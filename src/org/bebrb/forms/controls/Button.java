/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.Action;
import org.bebrb.forms.Control;

/**
 * Кнопка команды
 */
public interface Button extends Control<Void> {
	/**
	 * Надпись на кнопке
	 * @return не может быть null
	 */
	public String getCaption();
	/**
	 * Действие
	 * @return может быть null
	 */
	public Action getAction();
	/**
	 * Тест на способность удерживать состояние
	 * @return true означает то, что кнопка запоминает свое состояние после нажатия
	 */
	public boolean isHoldState();
	/**
	 * Тест на нажатие кнопки
	 * @return true если кнопка нажата. Если {@link #isHoldState()} является false возвращает всегда false.
	 */
	public boolean isDown();

}
