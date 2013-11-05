package org.bebrb.server;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

public class Context {

	public static void main(String[] args) {
		try {
			System.out.println("start...");
			URL urlXSD = System.class.getResource("org/bebrb/resources/shema/ds.xsd");
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			//factory.setSchema(arg0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
