/**
 * 
 */
package org.bebrb.data;

import java.util.List;
import java.util.Map;

/**
 * �������� ������
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
	 * ���� �� ����������� �����������
	 * @return true ���� �������� ������ ��� ������
	 */
	public boolean isReadOnly();
	
	/**
	 * ���� �� ����������� ��������� ������ 
	 * @return true ���� � �������� ����� ��������� ����� ������
	 */
	public boolean isCanAdd();

	/**
	 * ���� �� ����������� �������� ������� 
	 * @return true ���� � ��������� ����� ������� ������
	 */
	public boolean isCanDelete();
	
	/**
	 * ���� �� ����������� �������� ������ 
	 * @return true ���� � �������� ����� �������� ������
	 */
	public boolean isCanEdit();
	
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
	
	/**
	 * @return ������������ ������ ������ � �������
	 */
	public int getMaxSizeDataPage();
	
	/**
	 * ����� ������ �� �������� �����. 
	 * @param value �������� ����� ������� ������, �� ����� ���� null
	 * @param onServer ���� false, �� ����� �������������� � ������� ���������� � �������. ����� �� ���� ������� ��������� (����� ������������� ��������� �����)   
	 * @return null ���� ������ �� �������, ����� ������� ������
	 * @throws Exception 
	 */
	public Record findRecord(Object value, boolean onServer) throws Exception;
	
	/**
	 * ����� ������ �� �������� �����. ����� �������������� � ������� ���������� � ������� 
	 * @param value �������� ����� ������� ������, �� ����� ���� null
	 * @return null ���� ������ �� �������, ����� ������� ������
	 * @see #findRecord(Object, boolean)
	 */
	public Record findRecord(Object value) throws Exception;

	
	/**
	 * ����� ������ �� ��������� ���������.
	 * @param values �������� ��������� �� ������� ����� ������ 
	 * @param onServer ���� false, �� ����� �������������� � ������� ���������� � �������. ����� �� ���� ������� ��������� (����� ������������� ��������� �����)
	 * @return null ���� ������ �� �������, ����� ������� ������
	 * @throws Exception
	 */
	public Record findRecord(Map<Attribute,Object> values, boolean onServer) throws Exception;
	
	/**
	 * ����� ������ �� ��������� ���������. ����� �������������� � ������� ���������� � �������
	 * @param values �������� ��������� �� ������� ����� ������ 
	 * @return null ���� ������ �� �������, ����� ������� ������
	 * @throws Exception
	 * @see {@link #findRecord(Map, boolean)}
	 */
	public Record findRecord(Map<Attribute,Object> values) throws Exception;
	
	
	/**
	 * ������� ������ c ����� {@link Record.Type#Buffer} ��� �������������� �������� ������. ������ r ���������� � ������������ ������ 
	 * @param r ������ ��� ��������������
	 * @return �� ����� ���� null
	 * @throws Exception ���� ����� �������������� ����������
	 * @see #isReadOnly()
	 * @see #isCanEdit()
	 * 
	 */
	public Record edit(Record r) throws Exception;
	
	/**
	 * ������� ����� ������ c ����� {@link Record.Type#Buffer} ��� ����������.  
	 * @return �� ����� ���� null
	 * @throws Exception ���� ����� �������������� ����������
	 * @see #isReadOnly()
	 * @see #isCanAdd()
	 * 
	 */
	public Record add() throws Exception;
	
	
	/**
	 * ������� ������ c ����� {@link Record.Type#Buffer} ��� ��������. ������ r ���������� � ������������ ������ 
	 * @param r ������ ��� ��������
	 * @return �� ����� ���� null
	 * @throws Exception ���� ����� �������� ����������
	 * @see #isReadOnly()
	 * @see #isCanDelete()
	 * 
	 */
	public Record delete(Record r) throws Exception;

	/**
	 * ������� ������� ��������� �������. �� ��������� ������ �� ��� ��������� ������ c ����� {@link Record.Type#Buffer} ��� ��������. ����� ���������� {@link Record#commit()} 
	 * @param records ������ �������
	 * @param onvalidate �������� �������� ��� ��������� ��������
	 * @throws Exception ���� ����� �������� ����������
	 */
	public void delete(List<Record> records, OnValidate onvalidate) throws Exception;

	/**
	 * ������� ������� ��������� �������. �� ��������� ������ �� ��� ��������� ������ c ����� {@link Record.Type#Buffer} ��� ��������. ����� ���������� {@link Record#commit()}.
	 * ������������ ����������� �������� �����. �������� ���������� �� ������ ������. ��� ��������� ���������� �������� ����������� {@link #delete(List, OnValidate)} }    
	 * @param records ������ �������
	 * @throws Exception ���� ����� �������� ���������� ��� ������ �� ������ ��������
	 */
	public void delete(List<Record> records) throws Exception;
	
	/**
	 * �������� �������� ����� ������ 
	 * @param onvalidate ��������� ��� �������� �����
	 */
	public void addValidator(OnValidate onvalidate);
	
	
	/**
	 * ������� �������� ����� ������ 
	 * @param onvalidate ��������� ��� �������� �����
	 */
	public void removeValidator(OnValidate onvalidate);
	
	
}
