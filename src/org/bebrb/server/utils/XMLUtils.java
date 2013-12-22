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
	
	public static String getTextContent(Node node) {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
            case Node.ATTRIBUTE_NODE:
            case Node.ENTITY_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE: {
                StringBuffer sb = new StringBuffer();
                NodeList list = node.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    if (!isCommentOrProcessingInstruction(list.item(i)))
                        sb.append(getTextContent(list.item(i)));
                }
                return sb.toString();
            }
            case Node.TEXT_NODE:
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE: {
                return node.getNodeValue();
            }
            default:
                return null;
        }
	}
    private static boolean isCommentOrProcessingInstruction(Node node) {
        return node.getNodeType() == Node.COMMENT_NODE
                || node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE;
    }

}
