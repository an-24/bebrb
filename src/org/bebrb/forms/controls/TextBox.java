package org.bebrb.forms.controls;

import org.bebrb.forms.Control;

public interface TextBox extends Control<String> {
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

}
