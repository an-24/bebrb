/**
 * 
 */
package org.bebrb.forms;

import java.util.List;

import org.bebrb.data.Field;
import org.bebrb.data.OnValidateField;
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
	 * @return  не может быть null или пустым
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
	 * Тест на возможность управления группой элементов
	 * @return true если элемент может управлять группой
	 */
	public boolean isGroupManager();
	
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
	 * Форма ввода которой принадлежит элемент управления
	 * @return не может быть null
	 */
	public Form getForm();
	/**
	 * Элемент управления который является группирующим для данного элемента
	 * @return равен null для элементов которые располагаются прямо на форме
	 */
	public Control<?> getParent();
	/**
	 * Список элементов для который данный элемент является группирующим
	 * @return может быть null для элементов, которые не могут быть группирующими
	 */
	public List<Control<?>> getChildren();
	/**
	 * Добавляем элекмент в группу с которой ассоциирован данный элемент управления. 
	 * @param control новый элемент группы. Не может быть null
	 * @exception Exception если данный элемент не может быть группирующим
	 * @see #isGroupManager()
	 */
	public void addChild(Control<?> control) throws Exception;
	/**
	 * Удаляет элемент из группы
	 * @param control удаляемый элемент группы. Не может быть null
	 */
	public void removeChild(Control<?> control);
	/**
	 * Границы элемента относительно {@link #getParent()} или {@link #getForm()}
	 * @return не может быть null
	 */
	public Rect getBounds();
	/**
	 * Объект контроля ввода данных 
	 * @return может быть null
	 */
	public OnValidateField<T> getValidator();
	
	/**
	 * Устанавливает объект контроля ввода данных.  
	 * @param value может быть null. В этом сучае контроль снимается
	 */
	public void setValidator(OnValidateField<T> value);
	

}
