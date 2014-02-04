package org.bebrb.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.bebrb.data.Attribute;
import org.bebrb.data.Attribute.Type;
import org.bebrb.data.DataSource;
import org.bebrb.reference.ReferenceBookMetaData;
import org.bebrb.server.data.AttributeImpl;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ReferenceBookMetaDataImpl implements ReferenceBookMetaData {
	private String id;
	private String title;
	private boolean history;
	private ReferenceType type;
	private CacheControl cc;
	private List<Attribute> attrs = new ArrayList<Attribute>();
	private Attribute keyAttribute = null;
	private Attribute parentKey = null;
	private boolean сanChoiseFolder = false;
	private Date actualCacheDate;
	private Attribute attrFolder;
	private String valueFolder;
	private String orderFolder;
	private String masterTable;

	public ReferenceBookMetaDataImpl(Element el) throws SAXException {
		id = el.getAttribute("id");
		title = el.getAttribute("title");
		history = Boolean.parseBoolean(el.getAttribute("history"));
		type = ReferenceBookMetaData.ReferenceType.valueOf(el.getAttribute("type"));
		cc = DataSource.CacheControl.valueOf(el.getAttribute("cache-control"));
		if(cc==DataSource.CacheControl.IsModified)
			if(el.getAttribute("actualDate").isEmpty()) actualCacheDate =  new Date();
												   else actualCacheDate =  DatatypeConverter.parseDate(el.getAttribute("actualDate")).getTime();;
		// attributes
		XMLUtils.enumChildren(XMLUtils.findChild(el, "attributes"), new NotifyElement() {
			@Override
			public boolean notify(Element e) {
				try {
					attrs.add(new AttributeImpl(ReferenceBookMetaDataImpl.this,e));
				} catch (SAXException e1) {
					RuntimeException re = new RuntimeException(e1.getMessage());
					re.initCause(e1);
					throw re;
				}
				return false;
			}
		});
		if(type == ReferenceType.Hierarchy) {
			attrs.add(new AttributeImpl(ReferenceBookMetaDataImpl.this,
					HIERARCHY_CHILD_COUNT,
					null,Type.Integer,false,false,0));
		}
		// set key
		keyAttribute = findAttribute(el.getAttribute("key"));
		if(keyAttribute==null)
			throw new SAXException("For key attribute not found");
		((AttributeImpl)keyAttribute).setKey(true);
		// master table; optional
		masterTable = el.getAttribute("mastertable");
		if(masterTable.isEmpty()) masterTable = null;
		// Hierarchy
		if(type == ReferenceType.Hierarchy) {
			parentKey = findAttribute(el.getAttribute("parentkey"));
			if(parentKey==null)
				throw new SAXException("Parent key attribute not found");
			Element elfld = XMLUtils.findChild(el, "folder");
			if(elfld==null)
				throw new SAXException("\"folder\" element not found");
			attrFolder = findAttribute(elfld.getAttribute("attribute"));
			valueFolder = elfld.getAttribute("value");
			сanChoiseFolder = Boolean.parseBoolean(elfld.getAttribute("сanсhoise"));
			orderFolder = elfld.getAttribute("order");
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

	@Override
	public boolean isHistoryAvailable() {
		return history;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getReferenceTitle() {
		return title;
	}

	@Override
	public String getName() {
		return title;
	}

	@Override
	public ReferenceType getReferenceType() {
		return type;
	}

	@Override
	public Date getActualDate() {
		return actualCacheDate;
	}

	@Override
	public List<Attribute> getAttributes() {
		return attrs;
	}

	@Override
	public Attribute getKey() {
		return keyAttribute;
	}

	@Override
	public Attribute getParentKey() {
		return parentKey;
	}

	@Override
	public boolean isCanChoiseFolder() {
		return сanChoiseFolder;
	}

	@Override
	public CacheControl getCacheControl() {
		return cc;
	}

	public Attribute getAttrFolder() {
		return attrFolder;
	}

	public String getValueFolder() {
		return valueFolder;
	}

	public String getOrderFolder() {
		return orderFolder;
	}
	
	public String getMasterTable() {
		return masterTable;
	}


}
