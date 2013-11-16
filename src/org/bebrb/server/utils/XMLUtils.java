package org.bebrb.server.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	public static void enumChildren(Element parent, NotifyElement event) {
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


}
