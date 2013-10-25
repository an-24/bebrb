/**
 * 
 */
package org.bebrb.data;

import java.util.List;

/**
 * ������ � �������. ������ ������� �� �����
 * @author Andrey Klyuev
 */
public interface Record {
	
	/**
	 * ���� ������
	 * <ul>
	 * <li> {@link #Data} - ������� ������, ���������� � �������
	 * <li> {@link #Summary} - ������, ������� ��������� �� ������� � ����� ���������� ������ �� ������� ��� � ����� 
	 * <li> {@link #Buffer} - ������ � ������
	 * </ul>
	 * 
	 */
	public static enum Type{Data, Summary, Buffer};
	
	/**
	 * �����, � ������� ��������� ������
	 * <ul>
	 * <li> {@link #Browse} - ������� �����
	 * <li> {@link #Add} - ����� ����������
	 * <li> {@link #Edit} - ����� ���������
	 * <li> {@link #Delete} - ����� ��������
	 * </ul>
	 * @see Record#getMode()
	 */
	public static enum Mode {Browse,Add,Edit,Delete};
	
	
	/**
	 * ���������� �����, � ������� ��������� ������
	 * @return �� ����� ���� null
	 * @see Mode
	 */
	public Mode getMode();
	
	/**
	 * ������ ����� � ������. ������ ���� �������� ������.
	 * @return �� ����� ���� null
	 */
	public List<Field<?>> getFields();
	
	/**
	 * C����� �������� � ������
	 * @return �� ����� ���� null
	 */
	public List<Object> getValues();
	
	/**
	 * ���������� ����, ������� �������� ������
	 * @return �� ����� ���� null. ����� �������� ������ ��������� ����.
	 */
	public Field<?> getKey();
	
	/**
	 * ���������� ���� �� ��� ����� 
	 * @param fname ��� ����
	 * @return �� ����� ���� null
	 * @exception Exception � ������ ���� ���� � ������ fname �� ������� 
	 * @see #findField(String)
	 */
	public Field<?> fieldByName(String fname) throws Exception;
	
	/**
	 * ���������� ���� �� ��� ����� 
	 * @param fname ��� ����
	 * @return null � ������ ���� ���� � ������ fname �� �������
	 * @see #fieldByName(String)
	 */
	public Field<?> findField(String fname);
	
	/**
	 * ���������� ����� �������� ����������� ������
	 * @return ����� ���� null, ���� ������ ��������� � ������ ������
	 */
	public DataPage getDataPage();
	
	/**
	 * ����������� ��������� � ��������� ������. �������� �������� �� ������
	 * @exception Exception � ������, ���� ������ �� ������ ��������
	 * 
	 */
	public void commit() throws Exception;
	
}
