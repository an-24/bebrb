package org.bebrb.forms;

import java.util.Map;

/**
 * Действие с именем
 */
public interface Action {
	/**
	 * Имя действия уникальное в рамках всего контекста приложения
	 * @return не может быть null
	 */
	public String getName();
	/**
	 * Доступность действия
	 * @return true для доступного действия 
	 */
	public boolean isEnabled();
	/**
	 * Установить доступность действия
	 * @param v доступность
	 */
	public void setEnabled(boolean v);
	/**
	 * Выполнение действия
	 * @param control элемент управления, выполняющий действие
	 * @param params ноль или более аргументов-параметров
	 */
	public void execute(Control<?> control, Object ... params) throws Exception;

}
