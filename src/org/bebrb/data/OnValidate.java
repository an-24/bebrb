package org.bebrb.data;

import java.util.Set;

/**
 * Интерфейс контроля данных 
 * @author Andrey Klyuev
 *
 */
public interface OnValidate {
	/**
	 * Вызывается методом {@link Record#commit()}. Если метод не null
	 * возбуждается исключительная ситуация  
	 * @param r запись которая требует проверки. Тип записи - {@link Record.Type#Buffer} . Тип операции можно узнать из {@link Record#getMode()}
	 * @return null если ошибок нет
	 */
	public Set<InvalidData> validate(Record r);

}
