/**
 * 
 */
package org.bebrb.utils;

/**
 * Свойство элемента управления или его класса
 */
public class ControlProperty {
	private String name;
	private String value;
	
	public ControlProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	public int getInt() {
		return Integer.parseInt(value);
	}
	
	public boolean getBoolean() {
		return Boolean.parseBoolean(value);
	}

	public double getDouble() {
		return Double.parseDouble(value);
	}
}
