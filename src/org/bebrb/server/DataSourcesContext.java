package org.bebrb.server;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DataSourcesContext {
	private ApplicationContext application;

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
		Document doc = factory.newDocumentBuilder().parse(application.getBasePath()+"datasources.xml");
	}

}
