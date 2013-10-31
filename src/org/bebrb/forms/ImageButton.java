/**
 * 
 */
package org.bebrb.forms;

import org.bebrb.forms.controls.ImageBox;
import org.bebrb.forms.controls.Label;

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
}
