package org.bebrb.server.data;

import org.bebrb.data.DataSource;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData.ReferenceType;
import org.bebrb.reference.View;
import org.bebrb.server.utils.XMLUtils;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ViewImpl implements View {
	private String name;
	private String title;
	private ReferenceBook referenceBook;
	private boolean lazy;
	private int root = ReferenceBook.MAIN_ROOT_ID;
	private String sqlTxt;

	public ViewImpl(Element el, ReferenceBook ref) throws SAXException {
		this.referenceBook =ref;
		name = el.getAttribute("name");
		title = el.getAttribute("title");
		sqlTxt = XMLUtils.getTextContent(XMLUtils.findChild(el, "sql"));
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
	public DataSource getDatasource() throws Exception {
		return new DataSourceImpl(referenceBook, this, referenceBook.getMetaData().getActualDate());
	}

	@Override
	public void refresh() throws Exception {
		throw new Exception("Is not supported on server");
	}

	@Override
	public Integer getRoot() {
		if(referenceBook.getMetaData().getReferenceType()!=ReferenceType.Hierarchy) return null;
		return root;
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getSQL() {
		return sqlTxt;
	}

	@Override
	public boolean isLazy() {
		return lazy;
	}

	public ReferenceBook getReferenceBook() {
		return referenceBook;
	}
}
