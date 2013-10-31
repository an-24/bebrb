/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.Action;
import org.bebrb.forms.ControlGroup;

/**
 * Кнопка команды с рисунком
 */
public interface ImageButton extends ControlGroup<Void> {
	/**
	 * Надпись на кнопке
	 * @return не может быть null
	 */
	public Label getLabel();
	/**
	 * Картинки на кнопке. 
	 * @return  может быть null
	 */
	public ImageBox getImage();
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
}
