package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;

/**
 * Поле ввода для строки
 */
public interface TextBox extends ControlGroup<String> {
	/**
	 * Максимальная длина в символах
	 * @return 0 (по умолчанию) означает отсутствие контроля длины
	 */
	public int getMaxLength();
	/**
	 * Тест на режим ввода пароля
	 * @return если true то символы скрываются
	 */
	public boolean isPassword();
	/**
	 * Метка для поля ввода
	 * @return не может быть null
	 */
	public Label getLabel();
}
