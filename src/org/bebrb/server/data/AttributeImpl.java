package org.bebrb.server.data;

import java.util.List;
import java.util.Map;

import org.bebrb.data.Attribute;
import org.bebrb.data.BaseDataSet;
import org.bebrb.data.Field;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class AttributeImpl implements Attribute {
	private BaseDataSet ds;
	private String name;
	private String caption;
	private Type type;
	private boolean keyFlag;
	private boolean visible;
	private boolean mandatory;
	private int size = 0;
	private Attribute fkey;
	private String[] fkResolveNeeded = null;
	
	public AttributeImpl(BaseDataSet ds, Element el) throws SAXException {
		this.ds = ds;
		name = el.getAttribute("name");
		caption = el.getAttribute("caption");
		if(caption.startsWith("@"))
			caption = el.getAttribute(caption.substring(1));
		type = Type.valueOf(el.getAttribute("type"));
		visible = Boolean.parseBoolean(el.getAttribute("visible"));
		mandatory = Boolean.parseBoolean(el.getAttribute("mandatory"));
		if(type == Type.String) {
			if(!el.hasAttribute("size"))
				throw new SAXException("Missing attribute of \"size\" in element \""+name+"\"");
			size = Integer.parseInt(el.getAttribute("size"));
		}
		if(el.hasAttribute("fkey")) {
			fkResolveNeeded = el.getAttribute("fkey").split("\\.");
		}
	}

	public AttributeImpl(BaseDataSet ds, String name, String caption,
			Type type, boolean visible, boolean mandatory, int size) {
		this.ds = ds;
		this.name = name;
		this.caption = caption;
		this.type = type;
		this.visible = visible;
		this.mandatory = mandatory;
		this.size = size;
	}
	
	public void resolveForeignKey(Map<String,BaseDataSet> datasets) throws Exception {
		if(fkResolveNeeded==null) return;
		ds = datasets.get(fkResolveNeeded[0]);
		if(ds==null)
			throw new Exception("Dataset \""+fkResolveNeeded[0]+"\" not found");
		List<Attribute> attrs = ds.getAttributes();
		for (Attribute attribute : attrs) {
			if(attribute.getName().equals(fkResolveNeeded[1])) {
				fkey = attribute;
				return;
			}
		}
		throw new Exception("Attribute \""+fkResolveNeeded[0]+"."+fkResolveNeeded[1]+"\" not found");
	}

	@Override
	public BaseDataSet getDataSource() {
		return ds;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getCaption() {
		return caption;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public boolean isKey() {
		return keyFlag;
	}

	public void setKey(boolean keyFlag) {
		this.keyFlag = keyFlag;
	}

	@Override
	public Attribute getForeignKey() {
		return fkey;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public boolean isMandatory() {
		return mandatory;
	}

	@Override
	public int getMaxSizeChar() {
		return size;
	}
	
	@Override
	public String toString() {
		return name; 
	}

	public Field<?> createField() {
		return null;
		
	}

	@Override
	public int getFieldNo() {
		throw new RuntimeException("Not supported on server-side");
	}
	
}
