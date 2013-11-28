/**
 * 
 */
package org.bebrb.reference;

import org.bebrb.data.Attribute;
import org.bebrb.data.BaseDataSet;

/**
 * Информация о структуре и полях справочника
 *
 */
public interface ReferenceBookMetaData extends BaseDataSet {
	/**
	 * Типы справочников 
	 * <ul>
	 *  <li> {@link ReferenceType#Simple} - простой справочник, без историчности. Ключ неизменен. Представление единственное. 
	 *  <li> {@link ReferenceType#Linear} - плоский линейный справочник с историчностью. Поддерживаются мастер данные. Наличие представлений.
	 *  <li> {@link ReferenceType#Hierarchy} - иерархический справочник с историчностью. Поддерживаются мастер данные. Наличие представлений. 
	 * </ul>
	 */
	public static enum ReferenceType {Simple, Linear, Hierarchy};
	
	/**
	 * Тест на историчность
	 * @return  true, если по справочнику допускаются исторические данные 
	 */
	public boolean isHistoryAvailable(); 
	
	/**
	 * Имя справочника
	 * @return не может быть null
	 */
	public String getReferenceTitle();
	
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
	 * Атрибут-ссылка на родительский элемент
	 * @return является null везде кроме справочников с типом {@link ReferenceType#Hierarchy} 
	 */
	public Attribute getParentKey();
	
	/**
	 * В случае иерархического справочника определяет возможность выбора папки
	 * @return не имеет значения везде кроме справочников с типом {@link ReferenceType#Hierarchy} 
	 */
	public boolean isCanChoiseFolder();

}
