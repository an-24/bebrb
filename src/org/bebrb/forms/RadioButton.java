/**
 * 
 */
package org.bebrb.forms;

/**
 * Переключатель
 */
public interface RadioButton extends Control<Boolean> {
	/**
	 * Текст переключателя
	 * @return может быть null
	 */
	public String getCaption();
	/**
	 * Имя группировки. Все переключатели с одинаковой группировкой работают
	 * совместно. Группировка действует в рамках группирующего элемента, которой принадлежит
	 * переключатель.
	 * @return может быть null
	 */
	public String getGroup();
	/**
	 * Тест на расположение текстовой метки
	 * @return true для текстовой метки расположенной слева
	 */
	public boolean isLabelLeft();

}
