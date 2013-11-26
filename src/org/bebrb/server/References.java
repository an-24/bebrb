package org.bebrb.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.bebrb.reference.ReferenceBook;
import org.xml.sax.SAXException;

public class References {
	ApplicationContextImpl application;
	
	public References(ApplicationContextImpl application) throws IOException, SAXException, ParserConfigurationException {
		this.application = application;
	}

	public List<ReferenceBook> getReferences() {
		List<ReferenceBook> list = new ArrayList<ReferenceBook>();
		//TODO
		return list;
		
	}

}
