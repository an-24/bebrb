/**
 * 
 */
package org.bebrb.reference;

import java.util.Date;
import java.util.List;

import org.bebrb.data.Attribute;

/**
 * ���������� � ��������� � ����� �����������
 * @author Andrey Klyuev
 *
 */
public interface ReferenceBookMetaData {
	/**
	 * ���� ������������ 
	 * <ul>
	 *  <li> {@link ReferenceType#Discriminator} - ������� ����������, ��� ������������. ���� ���������. ������������� �����������. 
	 *  <li> {@link ReferenceType#Linear} - ������� �������� ���������� � �������������. �������������� ������ ������. ������� �������������.
	 *  <li> {@link ReferenceType#Hierarchy} - ������������� ���������� � �������������. �������������� ������ ������. ������� �������������. 
	 * </ul>
	 */
	public static enum ReferenceType {Discriminator, Linear, Hierarchy};
	
	/**
	 * ���� �� ������������
	 * @return  true, ���� �� ����������� ����������� ������������ ������ 
	 */
	public boolean isHistoryAvailable(); 
	
	/**
	 * ���������� ������������� �����������
	 * @return  �� ����� ���� null
	 */
	public String getReferenceId();
	
	/**
	 * ��� �����������. �������� ��������� ��������
	 * <ul>
	 *  <li> {@link ReferenceType#Discriminator} - ������� ����������, ��� ������������. ���� ���������. �������� �����������. 
	 *  <li> {@link ReferenceType#Linear} - ������� �������� ���������� � �������������. �������������� ������ ������. ������� �������������.
	 *  <li> {@link ReferenceType#Hierarchy} - ������������� ���������� � �������������. �������������� ������ ������. ������� �������������. 
	 * </ul>
	 * 
	 * @return �� ����� ���� null
	 */
	public ReferenceType getReferenceType();
	
	/**
	 * ���������� ���� �� ������� ���������� �������� ���������� ����������.
	 * @return ��� ���� {@link ReferenceType#Discriminator} ���������� null
	 */
	public Date getActualDate();
	
	/**
	 * ���������� ������ ��������� �����������, ������� ��������
	 * @return �� ����� ���� null
	 */
	public List<Attribute> getAttributes();
	
	/**
	 * �������� �������
	 * @return �� ����� ���� null
	 */
	public Attribute getKey();
	
	/**
	 * �������-������ �� ������������ �������
	 * @return �������� null ����� ����� ������������ � ����� {@link ReferenceType#Hierarchy} 
	 */
	public Attribute getParentKey();

}
