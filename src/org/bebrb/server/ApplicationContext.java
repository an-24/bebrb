package org.bebrb.server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ApplicationContext {
	private static ApplicationContext loadedApp;
	
	private String name;
	private Version version = null;
	private String title;

	private static Logger log = Logger.getLogger("bebrb");
	private DataSourcesContext datasources;
	private Map<String, ModuleContext> modules = new HashMap<String, ModuleContext>();
	private List<String> dataModules = new ArrayList<String>();

	
	public ApplicationContext(String name, Version version) {
		this.name = name;
		this.version = version;
		load();
	}

	public ApplicationContext(String name) {
		this.name = name;
		load();
	}
	
	public List<String> getDataModules() {
		return dataModules;
	}

	public static Logger getLog() {
		return log;
	}

	public String getTitle() {
		return title;
	}
	
	private synchronized void load() {
		try {
			log.info("start app["+name+"-"+(version==null?"default":version)+"] loading process...");
			loadedApp = this;
			loadApplication();
			loadDataSources();
			//TODO
			afterLoad();
			loadedApp = null;
			log.info("app loading process [ok]");
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error config", e);
		}
	}

	private void loadApplication() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder builder = createXMLBuider("/org/bebrb/resources/shema/app.xsd");
		Document doc = builder.parse(getBasePath()+"app.xml");
		Element root = doc.getDocumentElement();
		title = XMLUtils.findChild(root, "info").getAttribute("title");
		if(version==null) {
			String sver = XMLUtils.findChild(root, "versions").getAttribute("default");
			version = new Version(sver);
		}
		Element el = XMLUtils.findChild(XMLUtils.findChild(root, "versions"),"number",version.toString());
		XMLUtils.enumChildren(XMLUtils.findChild(el, "data"), new NotifyElement() {
			@Override
			public boolean notify(Element e) {
				dataModules.add(e.getAttribute("name"));
				return false;
			}
		});
	}

	public Map<String, ModuleContext> getModules() {
		return modules;
	}

	
	private void afterLoad() throws Exception {
		datasources.afterLoad();
	}

	public String getVersionBasePath(){
		return "applications"+File.separatorChar+getName()+
				File.separator+getName()+"-"+getVersion()+File.separator;
	}

	public String getBasePath(){
		return "applications"+File.separatorChar+getName()+
				File.separator;
	}
	
	private void loadDataSources() throws IOException, SAXException, ParserConfigurationException {
		log.info("+start datasources loading process...");
		datasources = new DataSourcesContext(this);
		log.info("-datasources loading process [ok]");
	}

	public String getName() {
		return name;
	}


	public Version getVersion() {
		return version;
	}

	public DataSourcesContext getDatasources() {
		return datasources;
	}
	
	public static DocumentBuilder createXMLBuider(String xsdPath) throws IOException, SAXException, ParserConfigurationException {
		URL xsd = System.class.getResource(xsdPath);
		if(xsd==null)
			throw new IOException("XSD shema not found "+xsdPath);
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setSchema(schema);
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new ErrorHandler() {
			
			@Override
			public void warning(SAXParseException exception) throws SAXException {
				ApplicationContext.getLog().warning(exception.getMessage());
			}
			
			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				throw new SAXException(exception);
			}
			
			@Override
			public void error(SAXParseException exception) throws SAXException {
				throw new SAXException(exception);
			}
		});
		return builder;
	}

	public static ApplicationContext getLoadingContext() {
		return loadedApp;
	}

	public static class Version {
		public final int major;
		public final int minor;
		public final int build;

		public Version(int major, int minor, int build) {
			this.major = major;
			this.minor = minor;
			this.build = build;
		}
		public Version(String v) {
			String[] vers = v.split("-");
			major = new Integer(vers[0]);
			minor = new Integer(vers[1]);
			build = new Integer(vers[2]);
		}
		
		public String toString() {
			return major+"-"+minor+"-"+build;
		}
	}

}
