/**
 * 
 */
package org.bebrb.forms.controls;

import org.bebrb.forms.ControlGroup;

/**
 * Группа кнопок
 *
 */
public interface GroupButtons extends ControlGroup<Void> {
	public int getCount();
	public Button getButton(int index);
	public boolean[] getState();
	public boolean isRadio();
}
