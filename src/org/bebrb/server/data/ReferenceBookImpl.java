package org.bebrb.server.data;

import java.util.HashMap;
import java.util.Map;

import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData;
import org.bebrb.reference.View;
import org.bebrb.server.ReferenceBookMetaDataImpl;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ReferenceBookImpl implements ReferenceBook {
	private ReferenceBookMetaDataImpl meta;
	private Map<String, View> views = new HashMap<String, View>();
	
	public ReferenceBookImpl(Element el) throws SAXException {
		meta = new ReferenceBookMetaDataImpl(el);
		XMLUtils.enumChildren(XMLUtils.findChild(el, "views"), new NotifyElement() {
			@Override
			public boolean notify(Element e) {
				try {
					View view = new ViewImpl(e);
					views.put(view.getName(), view);
				} catch (SAXException e1) {
					RuntimeException re = new RuntimeException(e1.getMessage());
					re.initCause(e1);
					throw re;
				}
				return false;
			}
		});
	}

	@Override
	public ReferenceBookMetaData getMetaData() {
		return meta;
	}

	@Override
	public Map<String, View> getViews() {
		return views;
	}

	@Override
	public View getDefaultView() {
		// TODO Auto-generated method stub
		return null;
	}

}
