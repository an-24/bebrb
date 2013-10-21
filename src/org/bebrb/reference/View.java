/**
 * 
 */
package org.bebrb.reference;

import org.bebrb.data.DataSource;

/**
 * ������������� �����������
 * @author Andrey Klyuev
 *
 */
public interface View {
	
	/**
	 * ���������� � ������ ����������� �������������
	 * @return
	 */
	public String getName();
	
	/**
	 * ���������� ������ ������������� �����������. ��� ������ ��������� �� �����
	 * ���������� �������� ���� �����������. ���� �� ������� ���������� ����� {@link #refresh()}
	 * @return �� ����� ���� null
	 * @throws Exception
	 */
	public DataSource getDataSet() throws Exception;
	/**
	 * ���������� ������ � ���� �����������. �������� � �������� ���������� � ������
	 * @throws Exception
	 */
	public void refresh() throws Exception;
}
