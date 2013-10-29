package org.bebrb.forms;

import java.util.List;

/**
 * Группирующий элемент 
 */
public interface ControlGroup extends Control<Void> {
	/**
	 * Элемент управления который является группирующим для данного элемента
	 * @return равен null для элементов которые располагаются прямо на форме
	 */
	public abstract ControlGroup getParent();

	/**
	 * Список элементов для который данный элемент является группирующим
	 * @return может быть null для элементов, которые не могут быть группирующими
	 */
	public abstract List<Control<?>> getChildren();

	/**
	 * Добавляем элекмент в группу с которой ассоциирован данный элемент управления. 
	 * @param control новый элемент группы. Не может быть null
	 * @exception Exception если данный элемент не может быть группирующим
	 * @see #isGroupManager()
	 */
	public abstract void addChild(Control<?> control) throws Exception;

	/**
	 * Удаляет элемент из группы
	 * @param control удаляемый элемент группы. Не может быть null
	 */
	public abstract void removeChild(Control<?> control);

}