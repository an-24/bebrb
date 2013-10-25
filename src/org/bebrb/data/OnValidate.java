package org.bebrb.data;

/**
 * ��������� �������� ������ 
 * @author Andrey Klyuev
 *
 */
public interface OnValidate {
	/**
	 * ���������� ������� {@link Record#commit()}. ���� ����� ����������
	 * false ������������ �������������� ��������  
	 * @param r ������ ������� ������� ��������. ��� ������ - {@link Record.Type#Buffer} . ��� �������� ����� ������ �� {@link Record#getMode()}
	 * @return null ���� ������ ���
	 */
	public InvalidData validate(Record r);

}
