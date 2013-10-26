/**
 * 
 */
package org.bebrb.forms;

import java.util.List;

/**
 * Форма ввода
 *
 */
public interface Form {
	
	/**
	 * Имя формы
	 * @return не может быть null или пустым
	 */
	public String getName();
	
	/**
	 * Заголовок формы
	 * @return может быть null
	 */
	public String getTitle();
	
	/**
	 * Список элементов управления, принадлежащих форме
	 * @return не может быть null
	 */
	public List<Control<?>> getControls();
	
	/**
	 * Список элементов управления, принадлежащих форме и непосредственно на ней расположенных.
	 * @return не может быть null
	 */
	public List<Control<?>> getChildren();
	
	/**
	 * Тест на модальность формы
	 * @return если true то форма модальная
	 */
	public boolean isModal();
	
	
	
	

}
