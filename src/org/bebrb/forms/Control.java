/**
 * 
 */
package org.bebrb.forms;

import org.bebrb.data.Field;
import org.bebrb.utils.Rect;

/**
 * Элемент управления на форме
 * @author Andrey Klyuev
 */
public interface Control<T> {
	/**
	 * Имя элемента управления
	 * @return не может быть null или пустым
	 */
	public String getName();
	/**
	 * Альтернативное имя элемента управления
	 * @return может быть null
	 */
	public String getTag();
	/**
	 * Класс элемента управления
	 * @return не может быть null
	 */
	public ControlClass getControlClass();
	/**
	 * Тест на возможность привлечения внимания пользователя
	 * @return true если элемент управления привлекает внимание
	 */
	public boolean isCatchEye();
	/**
	 * Тест на возможность ввода данных
	 * @return true если элемент управления только для чтения
	 */
	public boolean isReadOnly();
	/**
	 * Тест на видимость элемента управления
	 * @return true если элемент управления виден
	 */
	public boolean isVisible();
	/**
	 * Тест на заполненность элемента управления
	 * @return true если элемент управления пуст
	 */
	public boolean isEmpty();
	/**
	 * Тест на то, что пользователь изменил данные
	 * @return true если пользователь изменил данные 
	 */
	public boolean isModify();
	
	/**
	 * Тест на возможность передачи фокуса ввода любым способом 
	 * @return true если фокус передать можно
	 */
	public boolean isFocusable();
	/**
	 * Порядок в списке обхода по Tab/Shift+Tab
	 * @return -1 если элемент отсутсвует в этом списке и с помощью клавиатуры нелья получить фокус, 0 если порядок выбирается самой системой,больше 0 указывает на конкретное 
	 * место с списке 
	 */
	public int getTabIndexFromParent();
	/**
	 * Данные введенные пользователем фиксируются в поле ввода вызовом {@link Field#setValue(Object)}
	 */
	public void acceptInput();
	/**
	 * Введенные пользователем данные сбрасываются. Элемент управления должен показать данные, полученные вызовом {@link Field#getValue()}
	 */
	public void cancelInput();
	/**
	 * Поле в которое осуществляется ввод
	 * @return может быть null элемент управления не предназначен для редактирования поля 
	 */
	public Field<T> getField();
	
	/**
	 * Введенные данные
	 * @return может быть null
	 */
	public T getValue();
	/**
	 * Установить данные
	 * @param value може быть null
	 */
	public void setValue(T value);
	
	/**
	 * Форма ввода которой принадлежит элемент управления
	 * @return не может быть null
	 */
	public Form getForm();
	/**
	 * Границы элемента относительно {@link #getParent()} или {@link #getForm()}
	 * @return не может быть null
	 */
	public Rect getBounds();
	

}
