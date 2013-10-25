/**
 * 
 */
package org.bebrb.data;

import java.util.List;
import java.util.Map;

/**
 * Источник данных
 * 
 * @author Andrey Klyuev
 *
 */
public interface DataSource {
	
	/**
	 * Уникальный идентификатор источника
	 * @return
	 */
	public String getId();
	
	/**
	 * Тест на получение данных в момент обращения к ним
	 * @return если false тогда данные получаются сразу при вызове {@link #getDataPages()}, иначе
	 * данные получаются постранично при вызове  {@link DataPage#getRecords()}
	 */
	public boolean isLazy();
	
	/**
	 * Тест на возможность модификации
	 * @return true если источник только для чтения
	 */
	public boolean isReadOnly();
	
	/**
	 * Тест на возможность добавлять записи 
	 * @return true если в источник можно добавлять новые записи
	 */
	public boolean isCanAdd();

	/**
	 * Тест на возможность удаления записей 
	 * @return true если в источнике можно удалять записи
	 */
	public boolean isCanDelete();
	
	/**
	 * Тест на возможность изменять записи 
	 * @return true если в источник можно изменять записи
	 */
	public boolean isCanEdit();
	
	/**
	 * Возвращает список атрибутов источника данных
	 * @return не может быть null
	 */
	public List<Attribute> getAttributes() throws Exception;
	
	/**
	 * Список страниц
	 * @return  не может быть null
	 * @throws Exception
	 */
	public List<DataPage> getDataPages() throws Exception;
	
	/**
	 * @return максимальный размер пакета с данными
	 */
	public int getMaxSizeDataPage();
	
	/**
	 * Поиск записи по значению ключа. 
	 * @param value значение ключа искомой записи, не может быть null
	 * @param onServer если false, то поиск осуществляется в пакетах полученных с сервера. Иначе по всем пакетам источника (может потребоваться удаленный вызов)   
	 * @return null если запись не найдена, иначе искомая запись
	 * @throws Exception 
	 */
	public Record findRecord(Object value, boolean onServer) throws Exception;
	
	/**
	 * Поиск записи по значению ключа. Поиск осуществляется в пакетах полученных с сервера 
	 * @param value значение ключа искомой записи, не может быть null
	 * @return null если запись не найдена, иначе искомая запись
	 * @see #findRecord(Object, boolean)
	 */
	public Record findRecord(Object value) throws Exception;

	
	/**
	 * Поиск записи по значениям атрибутов.
	 * @param values значения атрибутов по которым нужно искать 
	 * @param onServer если false, то поиск осуществляется в пакетах полученных с сервера. Иначе по всем пакетам источника (может потребоваться удаленный вызов)
	 * @return null если запись не найдена, иначе искомая запись
	 * @throws Exception
	 */
	public Record findRecord(Map<Attribute,Object> values, boolean onServer) throws Exception;
	
	/**
	 * Поиск записи по значениям атрибутов. Поиск осуществляется в пакетах полученных с сервера
	 * @param values значения атрибутов по которым нужно искать 
	 * @return null если запись не найдена, иначе искомая запись
	 * @throws Exception
	 * @see {@link #findRecord(Map, boolean)}
	 */
	public Record findRecord(Map<Attribute,Object> values) throws Exception;
	
	
	/**
	 * Создает запись c типом {@link Record.Type#Buffer} для редактирования исходной записи. Данные r копируются в возвращаемую запись 
	 * @param r запись для редактирования
	 * @return не может быть null
	 * @throws Exception если режим редактирования недоступен
	 * @see #isReadOnly()
	 * @see #isCanEdit()
	 * 
	 */
	public Record edit(Record r) throws Exception;
	
	/**
	 * Создает новую запись c типом {@link Record.Type#Buffer} для добавления.  
	 * @return не может быть null
	 * @throws Exception если режим редактирования недоступен
	 * @see #isReadOnly()
	 * @see #isCanAdd()
	 * 
	 */
	public Record add() throws Exception;
	
	
	/**
	 * Создает запись c типом {@link Record.Type#Buffer} для удаления. Данные r копируются в возвращаемую запись 
	 * @param r запись для удаления
	 * @return не может быть null
	 * @throws Exception если режим удаления недоступен
	 * @see #isReadOnly()
	 * @see #isCanDelete()
	 * 
	 */
	public Record delete(Record r) throws Exception;

	/**
	 * Попытка удалить множество записей. На основании каждой из них создается запись c типом {@link Record.Type#Buffer} для удаления. Затем вызывается {@link Record#commit()} 
	 * @param records список записей
	 * @param onvalidate контроль удаления для групповой операции
	 * @throws Exception если режим удаления недоступен
	 */
	public void delete(List<Record> records, OnValidate onvalidate) throws Exception;

	/**
	 * Попытка удалить множество записей. На основании каждой из них создается запись c типом {@link Record.Type#Buffer} для удаления. Затем вызывается {@link Record#commit()}.
	 * Используется стандартный контроль ввода. Удаление происходит до первой ошибки. Есл небходимо продолжать операцию используйте {@link #delete(List, OnValidate)} }    
	 * @param records список записей
	 * @throws Exception если режим удаления недоступен или данные не прошли проверку
	 */
	public void delete(List<Record> records) throws Exception;
	
	/**
	 * Добавить контроль ввода данных 
	 * @param onvalidate интерфейс для контроля ввода
	 */
	public void addValidator(OnValidate onvalidate);
	
	
	/**
	 * Удалить контроль ввода данных 
	 * @param onvalidate интерфейс для контроля ввода
	 */
	public void removeValidator(OnValidate onvalidate);
	
	
}
