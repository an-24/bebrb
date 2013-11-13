package org.bebrb.server.data;

import java.util.Map;

import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData;
import org.bebrb.reference.View;
import org.bebrb.server.ReferenceBookMetaDataImpl;
import org.w3c.dom.Element;

public class ReferenceBookImpl implements ReferenceBook {
	private ReferenceBookMetaDataImpl meta;
	
	public ReferenceBookImpl(Element el) {
		meta = new ReferenceBookMetaDataImpl(el);
	}

	@Override
	public String getReferenceId() {
		return meta.getReferenceId();
	}

	@Override
	public ReferenceBookMetaData getMetaData() {
		return meta;
	}

	@Override
	public Map<String, View> getViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getDefaultView() {
		// TODO Auto-generated method stub
		return null;
	}

}
