/**
 * 
 */
package org.bebrb.forms;

/**
 * События формы
 */
public interface FormEvent {
	public void setOnLoad(OnFormLoad onformload);
	public void setSubmitAction(Action onformsubmit);
	public void setCancelAction(Action onformcancel);	
}
