package org.bebrb.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.bebrb.data.Attribute;
import org.bebrb.data.BaseDataSet;
import org.bebrb.data.DataSource;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData;
import org.bebrb.server.data.AttributeImpl;
import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.data.ReferenceBookImpl;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DataSources {
	private ApplicationContextImpl application;
	private List<ReferenceBook> refs = new ArrayList<ReferenceBook>();
	private List<DataSource> datasources = new ArrayList<DataSource>();
	private Map<String,BaseDataSet> indexDataSet = new HashMap<String,BaseDataSet>();
	private Logger log = Logger.getLogger("bebrb");
	private Map<String, ReferenceBook> refsHash = null;
	private Map<String, DataSource> dsHash = null;

	public DataSources(ApplicationContextImpl application) throws IOException, SAXException, ParserConfigurationException {
		this.application = application;
		load();
	}

	private void load() throws IOException, SAXException, ParserConfigurationException {
		for (String fname : application.getDataModules()) 
			loadDataModule(application.getVersionBasePath()+fname);
	}

	private void loadDataModule(String fname) throws IOException, SAXException,
			ParserConfigurationException {
		log.info("++load data module "+fname);
		DocumentBuilder builder = ApplicationContextImpl.createXMLBuider("/org/bebrb/resources/shema/ds.xsd");
		Document doc = builder.parse(fname);
		Element root = doc.getDocumentElement();
		log.info("+++load references...");
		XMLUtils.enumChildren(XMLUtils.findChild(root,"references"),new NotifyElement(){

			@Override
			public boolean notify(Element e) {
				log.info("  load reference "+e.getAttribute("id"));
				try {
					ReferenceBookImpl ref = new ReferenceBookImpl(e);
					ReferenceBookMetaData meta = ref.getMetaData();
					
					if(indexDataSet.get(meta.getId())!=null)
						throw new SAXException("Duplicate reference Id ["+meta.getId()+"]");
					
					refs.add(ref);
					indexDataSet.put(meta.getId(), meta);
				} catch (Exception e1) {
					RuntimeException re = new RuntimeException(e1.getMessage());
					re.initCause(e1);
					throw re;
				}
				return false;
			}
		});
		log.info("---load references [ok]");
		
		log.info("+++load datasources...");
		XMLUtils.enumChildren(XMLUtils.findChild(root,"datasources"),new NotifyElement(){

			@Override
			public boolean notify(Element e) {
				log.info("  load datasource "+e.getAttribute("id"));
				try {
					DataSource ds = new DataSourceImpl(e, DataSources.this);
					if(indexDataSet.get(ds.getId())!=null)
						throw new SAXException("Duplicate datasource Id ["+ds.getId()+"]");
					datasources.add(ds);
					indexDataSet.put(ds.getId(), ds);
				} catch (Exception e1) {
					RuntimeException re = new RuntimeException(e1.getMessage());
					re.initCause(e1);
					throw re;
				}
				return false;
			}
		});
		log.info("---load datasources [ok]");
		log.info("--load data module [ok]");
	}

	public ReferenceBook findReference(String id) {
		if(refsHash!=null) return refsHash.get(id);
		for (ReferenceBook ref : refs) {
			if(ref.getMetaData().getId().equals(id)) return ref;
		}
		return null;
	}

	public DataSource findDataSource(String id) {
		if(dsHash!=null) return dsHash.get(id);
		for (DataSource ds : datasources) {
			if(ds.getId().equals(id)) return ds;
		}
		return null;
	}
	
	public void afterLoad() throws Exception {
		log.info("DataSources after load...");
		refsHash = new HashMap<String,ReferenceBook>(refs.size());
		// for refs
		for (ReferenceBook ref : refs) {
			for (Attribute attr : ref.getMetaData().getAttributes()) 
				((AttributeImpl)attr).resolveForeignKey(indexDataSet);
			// create hash
			refsHash.put(ref.getMetaData().getId(),ref);
		}
		// for datasources
		dsHash = new HashMap<String,DataSource>(datasources.size());
		for (DataSource ds : datasources) {
			for (Attribute attr : ds.getAttributes()) 
				((AttributeImpl)attr).resolveForeignKey(indexDataSet);
			// create hash
			dsHash.put(ds.getId(),ds);
		}
		log.info("DataSources after load [ok]");
	}

	public List<ReferenceBook> getRefs() {
		return refs;
	}

	public List<DataSource> getDatasources() {
		return datasources;
	}


}
