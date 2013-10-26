/**
 * 
 */
package org.bebrb.forms;

import java.util.List;

import org.bebrb.utils.Color;
import org.bebrb.utils.ControlProperty;

/**
 * Определяет группу однотипных элементов.
 *
 */
public interface ControlClass {

	public String getName();
	
	public Color getBackgroundColor();
	public Color getForegroundColor();
	public Color getFontColor();
	
	public ControlProperty getControlProperty(String name); 
	public List<ControlProperty> getControlPropertys(String name); 
	

}
