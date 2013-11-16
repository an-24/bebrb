package org.bebrb.server.data;

import org.bebrb.data.DataSource;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.View;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ViewImpl implements View {
	
	private String name;
	private String title;
	private ReferenceBook ref;

	public ViewImpl(Element el, ReferenceBook ref) throws SAXException {
		this.ref =ref;
		name = el.getAttribute("name");
		title = el.getAttribute("title");
		
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public DataSource getDataSet() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

}
