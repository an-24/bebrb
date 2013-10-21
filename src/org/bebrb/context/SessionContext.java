/**
 * 
 */
package org.bebrb.context;

import java.util.logging.Logger;

/**
 * �������� ������
 * @author Andrey Klyuev
 *
 */
public interface SessionContext extends Context {

	/**
	 * ������� ��������� ������������, ���������� ��������� ��������������.
	 * @return ������� ������������. 
	 */
	public UserContext getActiveUserContext();
	
	/**
	 * �������� ������ ������ ��������� ������������. ����� ��������� {@link #getActiveUserContext()} ����� 
	 * ���������� null.
	 */
	public void logout();
	
	/**
	 * ���������� ������ ��� ����������� � ������ ������
	 * @return �� ����� ���� null
	 */
	public Logger getLogger();
}
