package org.bebrb.server;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataSourcesContext {
	private ApplicationContext application;
	private Logger log = Logger.getLogger("bebrb");

	public DataSourcesContext(ApplicationContext application) throws IOException, SAXException, ParserConfigurationException {
		this.application = application;
		load();
	}

	private void load() throws IOException, SAXException, ParserConfigurationException {
		String xsdPath = "/org/bebrb/resources/shema/ds.xsd";
		URL xsd = System.class.getResource(xsdPath);
		if(xsd==null)
			throw new IOException("XSD shema not found "+xsdPath);
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setSchema(schema);
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(application.getBasePath()+"datasources.xml");
		Element root = doc.getDocumentElement();
		log.info("load references...");
		Element el = findChild(root,"references");
		enumChildren(el,new NotifyElement(){
			@Override
			public boolean notify(Element e) {
				log.info("  load reference "+e.getAttribute("id"));
				return false;
			}
		});
		log.info("load references [ok]");
	}

	private Element findChild(Element parent, final String name) {
		NodeList nl = parent.getChildNodes();
		for (int i = 0, len = nl.getLength(); i < len; i++) {
			Node n = nl.item(i);
			if(n instanceof Element && ((Element)n).getNodeName().equalsIgnoreCase(name))
				return (Element) n;
		}
		return null;
	}
	
	private void enumChildren(Element parent, NotifyElement event) {
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
