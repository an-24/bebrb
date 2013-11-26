package org.bebrb.server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.bebrb.data.RemoteFunction;
import org.bebrb.server.data.RemoteFunctionImpl;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ModuleContext {
	private Logger log = Logger.getLogger("bebrb");
	private Map<String,RemoteFunction> funcs = new HashMap<String,RemoteFunction>();
	
	public ModuleContext(File file) throws IOException, SAXException, ParserConfigurationException {
		log.info("+++load module "+file.getAbsolutePath());
		DocumentBuilder builder = ApplicationContextImpl.createXMLBuider("/org/bebrb/resources/shema/module.xsd");
		Document doc = builder.parse(file.getAbsolutePath());
		Element root = doc.getDocumentElement();
		XMLUtils.enumChildren(root, new NotifyElement() {
			@Override
			public boolean notify(Element e) {
				try {
					funcs.put(e.getAttribute("name"), new RemoteFunctionImpl(e));
				} catch (Exception e1) {
					RuntimeException re = new RuntimeException(e1.getMessage());
					re.initCause(e1);
					throw re;
				}
				return false;
			}
		});
		log.info("---load module [ok]");
	}

	public RemoteFunction getFunction(String name) {
		return funcs.get(name);
	}

}
