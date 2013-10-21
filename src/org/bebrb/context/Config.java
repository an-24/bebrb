/**
 * 
 */
package org.bebrb.context;

/**
 * @author Andrey Klyuev
 *
 */
public interface Config {
	
	/**
	 * ���������� �������� ��������� � ������������ ������. ���� ��������� ���, ����������
	 * �������� �� ���������  
	 * @param name ��� ���������
	 * @param def �������� �� ���������
	 * @return �������� ��������� ��� �������� def 
	 */
	public String getParam(String name, String def);

	/**
	 * ������������� ��������� � ������ name �������� value 
	 * @param name ��� ���������
	 * @param value ����� �������� ���������
	 */
	public void setParam(String name, String value);
	
	
}
