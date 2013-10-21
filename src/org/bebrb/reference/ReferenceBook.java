/**
 * 
 */
package org.bebrb.reference;

import java.util.Map;

/**
 * ����������
 * @author Andrey Klyuev
 *
 */
public interface ReferenceBook {
	
	/**
	 * ���������� ������������� �����������
	 * @return  �� ����� ���� null
	 */
	public String getReferenceId();
	
	/**
	 * ���, ��������� � ����������� �����������
	 * @return �� ����� ���� null
	 */
	public ReferenceBookMetaData getMetaData();
	/**
	 * ��� ��������� ������������� �����������
	 * @return ������������� ������ �������������. �� ����� ���� null
	 */
	public Map<String, View> getViews();
	/**
	 * ������������� �����������, ����������� �� ��������� 
	 * @return �������������. �� ����� ���� null
	 */
	public View getDefaultView();

}
