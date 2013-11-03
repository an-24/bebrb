/**
 * 
 */
package org.bebrb.forms;

import java.util.List;

import org.bebrb.data.Record;

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
	 * Открывает форму в отдельной вкладке. Режим формы определяется из {@link Record#getMode()}
	 * @param r запись с которой ассоциирована форма ввода
	 */
	public void open(Record r);
	/**
	 * Открывает форму в отдельной вкладке.
	 */
	public void open();
	/**
	 * Открывает форму в модальном варианте поверх текущей вкладки. Режим формы определяется из {@link Record#getMode()}
	 * @param r запись с которой ассоциирована форма ввода
	 * @return true если пользователь сделад положительный выбор. Если пользователь отменил операцию
	 * возвращается false
	 */
	public boolean openForResult(Record r);
	/**
	 * Открывает форму в модальном варианте поверх текущей вкладки.
	 * @return true если пользователь сделад положительный выбор. Если пользователь отменил операцию
	 * возвращается false
	 */
	public boolean openForResult();
	/**
	 * Закрывает форму в режиме отмены операции.
	 * @param withoutQuestion если true, то измененения в данных не проверяются, пользователь не предупреждается 
	 */
	public void cancel(boolean withoutQuestion);
	/**
	 * Закрывает форму в режиме отмены операции со всеми необходимыми проверками
	 */
	public void cancel();
	/**
	 * Фиксация изменений 
	 */
	public void submit() throws Exception;
	/**
	 * Возвращает редактируемую запись
	 * @return может быть null
	 */
	public Record getRecord();

}
