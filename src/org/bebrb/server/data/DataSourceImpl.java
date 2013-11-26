package org.bebrb.server.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bebrb.data.Attribute;
import org.bebrb.data.DataPage;
import org.bebrb.data.DataSource;
import org.bebrb.data.OnValidate;
import org.bebrb.data.Record;
import org.bebrb.data.RemoteFunction;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.View;
import org.bebrb.server.DataSources;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DataSourceImpl implements DataSource {
	private String id;
	private boolean lazy;
	private CacheControl cc;
	private List<Attribute> attrs = new ArrayList<Attribute>();
	private Attribute keyAttribute = null;
	private RemoteFunction insRPC = null;
	private RemoteFunction updRPC = null;
	private RemoteFunction delRPC = null;
	
	private String sqlText = null;
	// TODO from cache
	private Date actualCacheDate = null;

	public DataSourceImpl(Element el,DataSources dscontext) throws Exception {
		id = el.getAttribute("id");
		lazy = Boolean.parseBoolean(el.getAttribute("lazy"));
		cc = DataSource.CacheControl.valueOf(el.getAttribute("cache-control"));
		
		Element refref = XMLUtils.findChild(el, "reference");
		if(refref!=null) {
			if(refref.hasChildNodes())
				throw new SAXException("Datasource "+id+" is not empty body. Required or not an empty attribute \"reference\" or non-empty body datasource");
			ReferenceBook rbook = dscontext.findReference(refref.getAttribute("id"));
			if(rbook==null)
				throw new SAXException("Reference book["+refref.getAttribute("id")+"] not found");
			View rbview = rbook.getViews().get(refref.getAttribute("view"));
			if(rbview==null)
				throw new SAXException("View["+refref.getAttribute("view")+"] in reference book["+refref.getAttribute("id")+"] not found");
			attrs.addAll(rbook.getMetaData().getAttributes());
			keyAttribute = rbook.getMetaData().getKey();
			insRPC = rbook.getInsertFunc();
			updRPC = rbook.getUpdateFunc();
			delRPC = rbook.getDeleteFunc();
			
		} else {
			sqlText = XMLUtils.getText(XMLUtils.findChild(el, "sql"));
			// attributes
			XMLUtils.enumChildren(XMLUtils.findChild(el, "attributes"), new NotifyElement() {
				@Override
				public boolean notify(Element e) {
					try {
						attrs.add(new AttributeImpl(e,DataSourceImpl.this));
					} catch (SAXException e1) {
						RuntimeException re = new RuntimeException(e1.getMessage());
						re.initCause(e1);
						throw re;
					}
					return false;
				}
			});
			// set key
			keyAttribute = findAttribute(el.getAttribute("key"));
			if(keyAttribute==null)
				throw new SAXException("For key attribute not found");
			((AttributeImpl)keyAttribute).setKey(true);
			
			Element efunc;
			
			efunc = XMLUtils.findChild(el, "insert");
			if(efunc!=null) insRPC = new RemoteFunctionImpl(efunc);

			efunc = XMLUtils.findChild(el, "update");
			if(efunc!=null) updRPC = new RemoteFunctionImpl(efunc);
			
			efunc = XMLUtils.findChild(el, "delete");
			if(efunc!=null) delRPC = new RemoteFunctionImpl(efunc);
		}
	}

	private Attribute findAttribute(String key) {
		for (Attribute attr : attrs) {
			if(attr.getName().equalsIgnoreCase(key)) {
				return attr;
			}
		}
		return null;
	}
	
	private void notSupportedOnServer() {
		throw new RuntimeException("Not supported on server-side");
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<Attribute> getAttributes() throws Exception {
		return attrs;
	}

	@Override
	public Attribute getKey() {
		return keyAttribute;
	}

	@Override
	public CacheControl getCacheControl() {
		return cc;
	}

	@Override
	public boolean isLazy() {
		return lazy;
	}

	@Override
	public boolean isReadOnly() {
		return insRPC==null && updRPC==null && delRPC==null;
	}

	@Override
	public boolean isCanAdd() {
		return insRPC!=null;
	}

	@Override
	public boolean isCanDelete() {
		return delRPC!=null;
	}

	@Override
	public boolean isCanEdit() {
		return updRPC!=null;
	}

	@Override
	public List<DataPage> getDataPages() throws Exception {
		//TODO
		return null;
	}

	@Override
	public int getMaxSizeDataPage() {
		return DEFAULT_PAGE_MAXSIZE;
	}

	@Override
	public Date getActualDate() {
		return  (cc != CacheControl.IsModified)?null:actualCacheDate;
	}
	
	@Override
	public Record findRecord(Object value, boolean onServer) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record findRecord(Object value) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public List<Record> findRecord(Map<Attribute, Object> values,
			boolean onServer) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public List<Record> findRecord(Map<Attribute, Object> values)
			throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record edit(Record r) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record add() throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record add(Record r) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record delete(Record r) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public void delete(List<Record> records, OnValidate onvalidate)
			throws Exception {
		notSupportedOnServer();
	}

	@Override
	public void delete(List<Record> records) throws Exception {
		notSupportedOnServer();
	}

	@Override
	public void addValidator(OnValidate onvalidate) {
		notSupportedOnServer();
	}

	@Override
	public void removeValidator(OnValidate onvalidate) {
		notSupportedOnServer();
	}


}
