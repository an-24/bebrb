/**
 * 
 */
package org.bebrb.context;

import org.bebrb.user.User;

/**
 * @author Andrey Klyuev
 *
 */
public interface UserContext extends Context {
	
	/**
	 * ���������� ������������
	 * @return �� ����� ���� null
	 */
	public User getUser();

}
