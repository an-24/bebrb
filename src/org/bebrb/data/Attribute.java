package org.bebrb.data;

/**
 * ������������ ������� � ��������� ������ 
 *
 */
public interface Attribute {
	/**
	 * ���� ��������� � ����������� 
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
}