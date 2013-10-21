/**
 * 
 */
package org.bebrb.reference;

import java.util.Date;
import java.util.List;

import org.bebrb.data.Attribute;

/**
 * Информация о структуре и полях справочника
 * @author Andrey Klyuev
 *
 */
public interface ReferenceBookMetaData {
	/**
	 * Типы справочников 
	 * <ul>
	 *  <li> {@link ReferenceType#Discriminator} - простой справочник, без историчности. Ключ неизменен. Представления отсутствуют. 
	 *  <li> {@link ReferenceType#Linear} - плоский линейный справочник с историчностью. Поддерживаются мастер данные. Наличие представлений.
	 *  <li> {@link ReferenceType#Hierarchy} - иерархический справочник с историчностью. Поддерживаются мастер данные. Наличие представлений. 
	 * </ul>
	 */
	public static enum ReferenceType {Discriminator, Linear, Hierarchy};
	
	/**
	 * Тест на историчность
	 * @return  true, если по справочнику допускаются исторические данные 
	 */
	public boolean isHistoryAvailable(); 
	
	/**
	 * Уникальный идентификатор справочника
	 * @return  не может быть null
	 */
	public String getReferenceId();
	
	/**
	 * Тип справочника. Возможны следующие варианты
	 * <ul>
	 *  <li> {@link ReferenceType#Discriminator} - простой справочник, без историчности. Ключ неизменен. Сценарии отсутствуют. 
	 *  <li> {@link ReferenceType#Linear} - плоский линейный справочник с историчностью. Поддерживаются мастер данные. Наличие представлений.
	 *  <li> {@link ReferenceType#Hierarchy} - иерархический справочник с историчностью. Поддерживаются мастер данные. Наличие представлений. 
	 * </ul>
	 * 
	 * @return не может быть null
	 */
	public ReferenceType getReferenceType();
	
	/**
	 * Возвращает дату на которую справочник содержит актуальную информацию.
	 * @return для типа {@link ReferenceType#Discriminator} возвращает null
	 */
	public Date getActualDate();
	
	/**
	 * Возвращает список атрибутов справочника, включая ключевой
	 * @return не может быть null
	 */
	public List<Attribute> getAttributes();
	
	/**
	 * Ключевой атрибут
	 * @return не может быть null
	 */
	public Attribute getKey();
	
	/**
	 * Атрибут-ссылка на родительский элемент
	 * @return является null везде кроме справочников с типом {@link ReferenceType#Hierarchy} 
	 */
	public Attribute getParentKey();

}
