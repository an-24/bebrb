/**
 * 
 */
package org.bebrb.data;

import java.util.List;

/**
 * ����� � �������. �������� ������ ������� �� �������, ������� ����� ���� ��������
 * ��������� � �� �����.
 * 
 * @author Andrey Klyuev
 *
 */
public interface DataPage {
	
	/**
	 * ���������� ���-�� �������, ���������� �������.   
	 * @return ���-�� ������� � ������. ���� ����� �� {@link #isAlive()} �� ������������ ���� 
	 */
	public int getSize();
	
	/**
	 * ������ �������, ���������� �������. ����
	 * ������ ��� �� ��������, �� �������������� ������� �� ���������  
	 * @return �� ����� ���� null
	 */
	public List<Record> getRecords() throws Exception;
	
	/**
	 * ���� �� ��������� ����� � ��������� ������
	 * @return true ���� ����� ���������, ����� false
	 * @throws Exception ���������, ���� ����� ��� �� ������� ������
	 */
	public boolean isEOF() throws Exception;
	
	/**
	 * ���� �� ��, ��� ����� ������� ��� ������
	 * @return true ����� ����� ��������, ����� false 
	 */
	public boolean isAlive();
	

}
