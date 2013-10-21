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
 * �������� ����������.
 * @author Andrey Klyuev
 *
 */
public interface ApplicationContext  extends Context {
	
	/**
	 * ����� ���������� � ����.
	 * @return URI � ���� ������
	 */
	public String getLocation();
	
	/**
	 * ��� ���������� ��� ������������.
	 * @return ��� ����������, �� ����� ���� null
	 */
	public String getTitle();
	
	
	/**
	 * ���� ���������� ��������� ���������� ������������� {@link #getLastLoginUser()} 
	 * @return ����-����� ���������� ���������. ����� ���� null
	 */
	public Date getLastLoginDate();
	
	/**
	 * ��������� ���������� ���������� ������������
	 * @return ������������. ����� ���� null
	 */
	public User getLastLoginUser();
	
	/**
	 * ������ �������� ������������� � ������ ������
	 * @return ��������� �������������. �� ����� ���� null
	 */
	public Set<User> getActiveUsers();
	
	/**
	 * ���������� ������� ������.
	 * @return ����� ���� null ���� ������������ �� ������������ � ������ �� �������
	 */
	public SessionContext getActiveSession();
	
	/**
	 * ���������� ����� ������ ����������
	 * @return ���������� ����� ������
	 */
	public int getVersion();
	
	/**
	 * ���������� �������� ����� �� ������ ������.
	 * @return �����, �������� � ������ ������. �� ����� ���� null
	 */
	public Form getActiveForm();
	
	
	/**
	 * ���������� ������� ����� ����������.
	 * @return ������� �����. �� ����� ���� null
	 */
	public Form getMainForm();
	
	/**
	 * ������ ������������ ������������
	 * @return
	 */
	public Set<ReferenceBook> getReferences(); 

}
