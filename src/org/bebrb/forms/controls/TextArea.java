package org.bebrb.forms.controls;

import org.bebrb.forms.Control;

public interface TextArea extends Control<String> {
	/**
	 * Тест на перенос строк
	 * @return true когла перенос строк включен
	 */
	public boolean isWrap();
	/**
	 * Максимальная длина в символах
	 * @return 0 (по умолчанию) означает отсутствие контроля длины
	 */
	public int getMaxLength();

}
