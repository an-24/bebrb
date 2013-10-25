package org.bebrb.data;

/**
 * ������������ ������� � ��������� ������ 
 *
 */
public interface Attribute {
	/**
	 * ���� ��������� 
	 * <ul>
	 * <li> {@link #Integer} - ����� ����� (32 �������)
	 * <li> {@link #String} - ������. ������������ ���-�� �������� � {@link Attribute#getMaxSizeChar()} 
	 * <li> {@link #Double} - ����� � ��������� ������ (32 �������)
	 * <li> {@link #Date} - ����/�����
	 * <li> {@link #Image} - ������� ��������� ������� {@link #Blob}, ������� ����� ���� ��������������� ��� �����������
	 * <li> {@link #Text} - ������� ��������� ������� {@link #Blob}, ������� ����� ���� ��������������� ��� �����
	 * <li> {@link #Blob} - �������� ������
	 * </ul>
	 */
	public static enum Type{Integer, String, Currency, Double, Date, Image, Text, Blob};
	
	
	/**
	 * �������� ������ � ������� ������������ ������ �������
	 * @return  �� ����� ���� null
	 */
	public String getDataSourceId();
	
	/**
	 * ��� ��������
	 * @return  �� ����� ���� null
	 */
	public String getName();
	/**
	 * ��� ��������
	 * @return  �� ����� ���� null
	 */
	public Attribute.Type getType();
	/**
	 * ���� �� ����
	 * @return true ���� ������� �������� ������
	 */
	public boolean isKey();
	
	/**
	 * ���� �� ������� ����
	 * @return ���������� ������� �� ������� ��������� ������ �������. ����� ���� null, ���� ������� �� �������� ������� ������
	 */
	public Attribute getForeignKey();
	
	/**
	 * ��������� ��������.
	 * @return true ���� ������� �����
	 */
	public boolean isVisible();
	
	/**
	 * �������������� ��������.
	 * @return true ���� ������� ����������
	 */
	public boolean isMandatory();
	
	/**
	 * ���������� ������������ ���-�� ��������, ������� ����� ���������� � ��������. ����� ����� ��� �������� ���� {@link #String}.
	 * ��� ��������� ����� ���������� 0. 
	 * @return ������ ������ � ��������
	 */
	public int getMaxSizeChar();
	
}