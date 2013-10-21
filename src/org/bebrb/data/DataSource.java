/**
 * 
 */
package org.bebrb.data;

import java.util.List;

/**
 * 
 * @author Andrey Klyuev
 *
 */
public interface DataSource {
	
	/**
	 * ���������� ������������� ���������
	 * @return
	 */
	public String getId();
	
	/**
	 * ���� �� ��������� ������ � ������ ��������� � ���
	 * @return ���� false ����� ������ ���������� ����� ��� ������ {@link #getDataPages()}, �����
	 * ������ ���������� ����������� ��� ������  {@link DataPage#getRecords()}
	 */
	public boolean isLazy();
	
	/**
	 * ���������� ������ ��������� ��������� ������
	 * @return �� ����� ���� null
	 */
	public List<Attribute> getAttributes() throws Exception;
	
	/**
	 * ������ �������
	 * @return  �� ����� ���� null
	 * @throws Exception
	 */
	public List<DataPage> getDataPages() throws Exception;

}
