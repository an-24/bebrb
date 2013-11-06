package test;

import java.io.IOException;
import java.io.InputStream;

public class TestDS {

	public static void main(String[] args) {
		try {
			System.out.println("start...");
			String xsdPath = "/org/bebrb/resources/shema/ds.xsd";
			InputStream xsd = System.class.getResourceAsStream(xsdPath);
			if(xsd==null)
				throw new IOException("XSD shema not found "+xsdPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
