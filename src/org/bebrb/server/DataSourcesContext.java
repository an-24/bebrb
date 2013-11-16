package org.bebrb.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.bebrb.data.Attribute;
import org.bebrb.data.BaseDataSet;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData;
import org.bebrb.server.data.AttributeImpl;
import org.bebrb.server.data.ReferenceBookImpl;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DataSourcesContext {
	private ApplicationContext application;
	private List<ReferenceBook> refs = new ArrayList<ReferenceBook>();
	private Map<String,BaseDataSet> indexDataSet = new HashMap<String,BaseDataSet>();
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
		Element el = XMLUtils.findChild(root,"references");
		XMLUtils.enumChildren(el,new NotifyElement(){

			@Override
			public boolean notify(Element e) {
				log.info("  load reference "+e.getAttribute("id"));
				try {
					ReferenceBookImpl ref = new ReferenceBookImpl(e);
					ReferenceBookMetaData meta = ref.getMetaData();
					refs.add(ref);
					indexDataSet.put(meta.getId(), meta);
				} catch (SAXException e1) {
					RuntimeException re = new RuntimeException(e1.getMessage());
					re.initCause(e1);
					throw re;
				}
				return false;
			}
		});
		log.info("load references [ok]");
	}

	public void afterLoad() throws Exception {
		log.info("DataSources after load...");
		// for refs
		for (ReferenceBook ref : refs) {
			for (Attribute attr : ref.getMetaData().getAttributes()) 
				((AttributeImpl)attr).resolveForeignKey(indexDataSet);
		}
		log.info("DataSources after load [ok]");
	}

}
