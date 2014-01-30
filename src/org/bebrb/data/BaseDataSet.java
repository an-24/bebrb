package org.bebrb.data;

import java.util.Date;
import java.util.List;

public interface BaseDataSet {

	public static enum CacheControl {None,WithinSession,IsModified};
	
	/**
	 * Unique data source id
	 * @return can not be null
	 */
	public String getId();

	/**
	 * Name of data source 
	 * @return can not be null
	 */
	public String getName();
	
	/**
	 * Возвращает список атрибутов источника данных
	 * @return не может быть null
	 */
	public List<Attribute> getAttributes();
	/**
	 * Ключевой атрибут
	 * @return не может быть null
	 */
	public Attribute getKey();
	/**
	 * Тип кэширования данных на клиенте
	 * @return  не может быть null
	 */
	public DataSource.CacheControl getCacheControl();
	/**
	 * Возвращает дату на которую справочник содержит актуальную информацию.
	 * @return для типа не {@link CacheControl#IsModified} возвращает null
	 */
	public Date getActualDate();
}
