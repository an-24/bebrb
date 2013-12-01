package org.bebrb.server.utils;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLUtils {
	
	public static Element findChild(Element parent, final String name) {
		NodeList nl = parent.getChildNodes();
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			Node n = nl.item(i);
			if(n instanceof Element && ((Element)n).getNodeName().equalsIgnoreCase(name))
				return (Element) n;
		}
		return null;
	}

	public static Element findChild(Element parent, final String attrName, String attrValue) {
		NodeList nl = parent.getChildNodes();
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			Node n = nl.item(i);
			if(n instanceof Element && ((Element)n).getAttribute(attrName).equals(attrValue))
				return (Element) n;
		}
		return null;
	}

	public static Element findChild(Element parent, String tagname, final String attrName, String attrValue) {
		NodeList nl = parent.getChildNodes();
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			Node n = nl.item(i);
			if(n instanceof Element && tagname.equals(n.getNodeName()) && 
			  ((Element)n).getAttribute(attrName).equals(attrValue))
				return (Element) n;
		}
		return null;
	}
	
	public static void enumChildren(Element parent, NotifyElement event) {
		if(parent==null) return;
		NodeList nl = parent.getChildNodes();
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			Node n = nl.item(i);
			if(n instanceof Element)
				if(event.notify((Element) n)) return;
		}
	}
	
	public interface NotifyElement {
		public boolean notify(Element e);
	}
	
	public static String getText(Element e) {
		Node n = e.getFirstChild();
		if(n instanceof Text || n instanceof CDATASection) return (String)((Text) n).getWholeText();
		return null;
	}


}
