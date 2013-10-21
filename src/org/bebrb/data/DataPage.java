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
public interface DataPage {
	/**
	 * @return ������������ ������ ������
	 */
	public int getMaxSize();
	
	/**
	 * ���������� ���-�� �������, ���������� �������. ����
	 * ������ ��� �� ��������, �� �������������� ������� �� ���������  
	 * @return ������ ������� � ������
	 */
	public int getSize()  throws Exception;
	
	/**
	 * ������ �������, ���������� �������. ����
	 * ������ ��� �� ��������, �� �������������� ������� �� ���������  
	 * @return �� ����� ���� null
	 */
	public List<Record> getRecords() throws Exception;

}
