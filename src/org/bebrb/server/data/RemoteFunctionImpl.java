package org.bebrb.server.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.bebrb.server.ApplicationContext;
import org.bebrb.server.ModuleContext;
import org.bebrb.data.Argument;
import org.bebrb.data.RemoteFunction;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class RemoteFunctionImpl implements RemoteFunction {

	private String name;
	protected Map<String, Argument> arguments =  new HashMap<String, Argument>();
	private String body;

	public RemoteFunctionImpl(Element el) throws SAXException, IOException, ParserConfigurationException {
		if(el.hasAttribute("ref") && el.hasChildNodes())
			throw new SAXException("Remote function "+el.getNodeName()+" is not empty body. Required or not an empty attribute \"ref\" or non-empty body functions");
		if(el.hasAttribute("ref")) {
			RemoteFunction reffunc = resolveRefFunc(el.getAttribute("ref"),el);
			name = reffunc.getName();
			List<Argument> rargs = reffunc.getArguments();
			for (Argument arg : rargs) {
				arguments.put(arg.getName(), arg);
			}
		} else {
			XMLUtils.enumChildren(XMLUtils.findChild(el, "arguments"), new NotifyElement() {
				@Override
				public boolean notify(Element e) {
					Argument arg = new ArgumentImpl(e,RemoteFunctionImpl.this);
					arguments.put(arg.getName(), arg);
					return false;
				}
			});
			name = el.getAttribute("name");
			body = XMLUtils.getText(el);
		}
	}

	private RemoteFunction resolveRefFunc(String fname,Element el) throws SAXException, IOException, ParserConfigurationException {
		String[] cNames = fname.split("\\.");
		if(cNames.length!=2) 
			throw new SAXException("Invalid format attribute \"ref\"");
		String uri = el.getOwnerDocument().lookupNamespaceURI(cNames[0]);
		ApplicationContext appContext = ApplicationContext.getLoadingContext(); 
		ModuleContext module = appContext.getModules().get(uri);
		if(module==null) {
			String baseuri = el.getOwnerDocument().getBaseURI();
			File f = new File(new File(new URL(baseuri).getPath()).getParent(),uri);
			module = new ModuleContext(f);
			appContext.getModules().put(uri, module);
		}
		return module.getFunction(cNames[1]);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Argument> getArguments() {
		ArrayList<Argument> list = new ArrayList<Argument>();
		list.addAll(arguments.values());
		return list;
	}

	@Override
	public String getText() {
		return body;
	}

}
