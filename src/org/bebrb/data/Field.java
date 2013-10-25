/**
 * 
 */
package org.bebrb.data;

/**
 * ���� ��������� ������.
 * @author Andrey Klyuev
 *
 * @param <T> ��� ����
 */
public interface Field<T> {
	/**
	 * �������� ���� � ������
	 * @return ����� ���� null ��� �����, ������� ����� �������������� ��������
	 */
	public T getValue();
	
	/**
	 * ����� ���� � ������
	 * @return ����� ����� �� 0 �� ���-�� ����� � ������, ����������� �� �������
	 */
	public int getIndex();
	
	/**
	 * �������� ��������������� ������� ����
	 * @return �� ����� ���� null
	 */
	public Attribute getAttribute();

}
