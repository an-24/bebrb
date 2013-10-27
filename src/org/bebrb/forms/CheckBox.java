package org.bebrb.forms;

/**
 * Флажок опции 
 *
 */
public interface CheckBox extends Control<Boolean> {
	/**
	 * Текст флажка
	 * @return может быть null
	 */
	public String getCaption();
	/**
	 * Тест на расположение текстовой метки
	 * @return true для текстовой метки расположенной слева
	 */
	public boolean isLabelLeft();

}
