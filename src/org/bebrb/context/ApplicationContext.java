/**
 * 
 */
package org.bebrb.context;

import java.util.Date;
import java.util.Set;

import org.bebrb.data.DataSource;
import org.bebrb.forms.Form;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.user.User;

/**
 * Контекст приложения.
 * @author Andrey Klyuev
 *
 */
public interface ApplicationContext  extends Context {
	
	/**
	 * Адрес приложения в сети.
	 * @return URI в виде строки
	 */
	public String getLocation();
	
	/**
	 * Имя приложения для пользователя.
	 * @return имя приложения, не может быть null
	 */
	public String getTitle();
	
	
	/**
	 * Дата последнего посещения приложения пользователем {@link #getLastLoginUser()} 
	 * @return дата-время последнего посещения. Может быть null
	 */
	public Date getLastLoginDate();
	
	/**
	 * Последний посетивший приложение пользователь
	 * @return пользователь. Может быть null
	 */
	public User getLastLoginUser();
	
	/**
	 * Список активных пользователей в момент вызова
	 * @return множество пользователей. Не может быть null
	 */
	public Set<User> getActiveUsers();
	
	/**
	 * Возвращает текущую сессию.
	 * @return может быть null если пользователь не автооризован и сессия не открыта
	 */
	public SessionContext getActiveSession();
	
	/**
	 * Возвращает номер версии приложения
	 * @return порядковый номер версии
	 */
	public int getVersion();
	
	/**
	 * Возвращает активную форму на момент вызова.
	 * @return форма, активная в данный момент. Не может быть null
	 */
	public Form getActiveForm();
	
	
	/**
	 * Возвращает главную форму приложения.
	 * @return главная форма. Не может быть null
	 */
	public Form getMainForm();
	
	/**
	 * Список используемых справочников
	 * @return
	 */
	public Set<ReferenceBook> getReferences();
	
	
	/**
	 * Список используемых источников данных. Список источников содержит и источники,
	 * представляющие справочники.
	 * @return
	 */
	public Set<DataSource> getDataSources(); 

}
