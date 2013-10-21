/**
 * 
 */
package org.bebrb.context;

import java.util.Date;
import java.util.Set;

import org.bebrb.forms.Form;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.user.User;

/**
 *  онтекст приложени€.
 * @author Andrey Klyuev
 *
 */
public interface ApplicationContext  extends Context {
	
	/**
	 * јдрес приложени€ в сети.
	 * @return URI в виде строки
	 */
	public String getLocation();
	
	/**
	 * »м€ приложени€ дл€ пользовател€.
	 * @return им€ приложени€, не может быть null
	 */
	public String getTitle();
	
	
	/**
	 * ƒата последнего посещени€ приложени€ пользователем {@link #getLastLoginUser()} 
	 * @return дата-врем€ последнего посещени€. ћожет быть null
	 */
	public Date getLastLoginDate();
	
	/**
	 * ѕоследний посетивший приложение пользователь
	 * @return пользователь. ћожет быть null
	 */
	public User getLastLoginUser();
	
	/**
	 * —писок активных пользователей в момент вызова
	 * @return множество пользователей. Ќе может быть null
	 */
	public Set<User> getActiveUsers();
	
	/**
	 * ¬озвращает текущую сессию.
	 * @return может быть null если пользователь не автооризован и сесси€ не открыта
	 */
	public SessionContext getActiveSession();
	
	/**
	 * ¬озвращает номер версии приложени€
	 * @return пор€дковый номер версии
	 */
	public int getVersion();
	
	/**
	 * ¬озвращает активную форму на момент вызова.
	 * @return форма, активна€ в данный момент. Ќе может быть null
	 */
	public Form getActiveForm();
	
	
	/**
	 * ¬озвращает главную форму приложени€.
	 * @return главна€ форма. Ќе может быть null
	 */
	public Form getMainForm();
	
	/**
	 * —писок используемых справочников
	 * @return
	 */
	public Set<ReferenceBook> getReferences(); 

}
