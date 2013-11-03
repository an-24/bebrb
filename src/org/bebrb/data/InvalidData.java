package org.bebrb.data;

import org.bebrb.forms.Control;

/**
 * Информация об ошибке ввода 
 */

public class InvalidData {
	
	private String message;
	private Attribute attribute;
	private Control<?> control;
	
	
	public InvalidData(String message, Attribute attribute, Control<?> control) {
		super();
		this.message = message;
		this.attribute = attribute;
		this.control = control;
	}

	public InvalidData(String message, Attribute attribute) {
		this(message,attribute,null);
	}

	public InvalidData(String message) {
		this(message,null,null);
	}
	
	public String getMessage() {
		return message;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public Control<?> getControl() {
		return control;
	}

}
