package org.bebrb.data;

import java.util.List;

public interface BaseDataSet {

	public static enum CacheControl {None,WithinSession,IsModified};
	
	/**
	 * Уникальный идентификатор источника
	 * @return не может быть null
	 */
	public String getId();
	
	/**
	 * Возвращает список атрибутов источника данных
	 * @return не может быть null
	 */
	public List<Attribute> getAttributes() throws Exception;
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
}
