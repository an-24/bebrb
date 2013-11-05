package org.bebrb.forms;

import java.util.List;

import org.bebrb.utils.Align;

/**
 * Группирующий элемент 
 */
public interface ControlGroup<T> extends Control<T> {
	/**
	 * Элемент управления который является группирующим для данного элемента
	 * @return равен null для элементов которые располагаются прямо на форме
	 */
	public abstract ControlGroup<?> getParent();

	/**
	 * Список элементов для который данный элемент является группирующим
	 * @return может быть null для элементов, которые не могут быть группирующими
	 */
	public abstract List<Control<?>> getChildren();

	/**
	 * Добавляем элекмент в группу с которой ассоциирован данный элемент управления. 
	 * @param control новый элемент группы. Не может быть null
	 * @exception Exception если данный элемент не может менять состав в силу установки блокировки
	 * @see #isLockedChildren()
	 */
	public abstract void addChild(Control<?> control) throws Exception;

	/**
	 * Удаляет элемент из группы
	 * @param control удаляемый элемент группы. Не может быть null
	 * @exception Exception если данный элемент не может менять состав в силу установки блокировки
	 * @see #isLockedChildren()
	 */
	public abstract void removeChild(Control<?> control) throws Exception;
	
	/**
	 * Тест на блокировку списка дочерних элементов
	 * @return true означает запрет на изменение состава дочерних элементов
	 */
	public boolean isLockedChildren();
	
	/**
	 * В контексте группирующего элемента выравнивание затрагивает элементы группы {@link #getChildren()}
	 * @return не может быть null
	 */
	public Align.Horizontal getHorizontalAlign();
	/**
	 * В контексте группирующего элемента выравнивание затрагивает элементы группы {@link #getChildren()}
	 * @return не может быть null
	 */
	public Align.Vertical getVerticalAlign();
	

}