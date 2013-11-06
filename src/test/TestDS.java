package test;

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;


public class TestDS {

	public static void main(String[] args) {
		try {
			System.out.println("start...");
			String xsdPath = "/org/bebrb/resources/shema/ds.xsd";
			URL xsd = System.class.getResource(xsdPath);
			if(xsd==null)
				throw new IOException("XSD shema not found "+xsdPath);
			Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd); 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setSchema(schema);
			factory.setValidating(false);
			Document doc = factory.newDocumentBuilder().parse("src/test/datasources.xml");
			
			System.out.println("[ok]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
