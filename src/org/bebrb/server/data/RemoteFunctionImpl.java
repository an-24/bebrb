package org.bebrb.server.data;

import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.xml.parsers.ParserConfigurationException;

import org.bebrb.server.ApplicationContextImpl;
import org.bebrb.server.ModuleContext;
import org.bebrb.data.Argument;
import org.bebrb.data.RemoteFunction;
import org.bebrb.server.net.ExecuteException;
import org.bebrb.server.utils.DBUtils;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class RemoteFunctionImpl implements RemoteFunction {

	private String name;
	protected List<Argument> arguments =  new ArrayList<>();
	private String body;

	public RemoteFunctionImpl(Element el) throws SAXException, IOException, ParserConfigurationException {
		if(el.hasAttribute("ref") && el.hasChildNodes())
			throw new SAXException("Remote function "+el.getNodeName()+" is not empty body. Required or not an empty attribute \"ref\" or non-empty body functions");
		if(el.hasAttribute("ref")) {
			RemoteFunction reffunc = resolveRefFunc(el.getAttribute("ref"),el);
			List<Argument> rargs = reffunc.getArguments();
			arguments.addAll(rargs);
			name = reffunc.getName();
			body = reffunc.getText();
		} else {
			XMLUtils.enumChildren(XMLUtils.findChild(el, "arguments"), new NotifyElement() {
				@Override
				public boolean notify(Element e) {
					Argument arg = new ArgumentImpl(e,RemoteFunctionImpl.this);
					arguments.add(arg);
					return false;
				}
			});
			name = el.getAttribute("name");
			body = XMLUtils.getTextContent(el);
		}
	}

	private RemoteFunction resolveRefFunc(String fname,Element el) throws SAXException, IOException, ParserConfigurationException {
		String[] cNames = fname.split("\\.");
		if(cNames.length!=2) 
			throw new SAXException("Invalid format attribute \"ref\"");
		String uri = el.getOwnerDocument().lookupNamespaceURI(cNames[0]);
		ApplicationContextImpl appContext = ApplicationContextImpl.getLoadingContext(); 
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
		return arguments;
	}

	@Override
	public String getText() {
		return body;
	}

	public Object exec(List<Object> args, Connection con) throws Exception {
		if(args==null) args = new ArrayList<>();
		// cast arguments
		List<Object> cargs = new ArrayList<>(args.size());
		if(!args.isEmpty()) {
			for (int i = 0, len = args.size(); i < len; i++) {
				cargs.add(DBUtils.castFromObject(args.get(i), arguments.get(i).getType()));
			}
		}
		// call
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine jsEngine = manager.getEngineByName("JavaScript");
		StringBuffer sfunc = new StringBuffer("function rpc_func(");
		Object[] values = new Object[cargs.size()];
		for (int i = 0, len = cargs.size(); i < len; i++) {
			sfunc.append(arguments.get(i).getName());
			if(i<len-1) sfunc.append(',');
			values[i] = cargs.get(i);
		}
		sfunc.append(")");
		sfunc.append(body);
		Bindings bindings = new SimpleBindings();
		bindings.put("connection", con);
		jsEngine.setBindings(bindings,ScriptContext.GLOBAL_SCOPE);
		
		con.setAutoCommit(false);
		try {
			jsEngine.eval(sfunc.toString());
	 		Invocable invocable = (Invocable) jsEngine;
	 		Object result = invocable.invokeFunction("rpc_func",values);
	 		con.commit();
			return result; 
		} catch (ScriptException e) {
			con.rollback();
			throw e;
		}
		
	}

}
