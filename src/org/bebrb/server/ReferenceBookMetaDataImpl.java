package org.bebrb.server;

import java.util.Date;
import java.util.List;

import org.bebrb.data.Attribute;
import org.bebrb.data.DataSource;
import org.bebrb.data.DataSource.CacheControl;
import org.bebrb.reference.ReferenceBookMetaData;
import org.w3c.dom.Element;

public class ReferenceBookMetaDataImpl implements ReferenceBookMetaData {
	private String id;
	private String title;
	private boolean history;
	private ReferenceType type;
	private CacheControl cc;

	public ReferenceBookMetaDataImpl(Element el) {
		id = el.getAttribute("id");
		title = el.getAttribute("title");
		history = Boolean.parseBoolean(el.getAttribute("history"));
		type = ReferenceBookMetaData.ReferenceType.valueOf(el.getAttribute("type"));
		cc = DataSource.CacheControl.valueOf(el.getAttribute("cache-control"));
	}

	@Override
	public boolean isHistoryAvailable() {
		return history;
	}

	@Override
	public String getReferenceId() {
		return id;
	}

	@Override
	public String getReferenceTitle() {
		return title;
	}

	@Override
	public ReferenceType getReferenceType() {
		return type;
	}

	@Override
	public Date getActualDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Attribute> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attribute getParentKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCanChoiseFolder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CacheControl getCacheControl() {
		return cc;
	}

}
