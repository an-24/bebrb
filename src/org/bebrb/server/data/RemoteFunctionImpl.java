package org.bebrb.server.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.xml.parsers.ParserConfigurationException;

import org.bebrb.data.Argument;
import org.bebrb.data.RemoteFunction;
import org.bebrb.server.ApplicationContextImpl;
import org.bebrb.server.ModuleContext;
import org.bebrb.server.utils.DBUtils;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class RemoteFunctionImpl implements RemoteFunction {

	private String name;
	protected List<Argument> arguments =  new ArrayList<>();
	private String body;
	private ScriptEngine jsEngine;
	private Element confElement;

	public RemoteFunctionImpl(Element el) throws SAXException, IOException, ParserConfigurationException {
		this.confElement = el;
		if(el.hasAttribute("ref") && el.hasChildNodes())
			throw new SAXException("Remote function "+el.getNodeName()+" is not empty body. Required or not an empty attribute \"ref\" or non-empty body functions");
		if(el.hasAttribute("ref")) {
			RemoteFunction reffunc = resolveRefFunc(el.getAttribute("ref"));
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

	private RemoteFunction resolveRefFunc(String fname) throws SAXException, IOException, ParserConfigurationException {
		String[] cNames = fname.split("\\.");
		if(cNames.length!=2) 
			throw new SAXException("Invalid format attribute \"ref\"");
		String uri = confElement.getOwnerDocument().lookupNamespaceURI(cNames[0]);
		ApplicationContextImpl appContext = ApplicationContextImpl.getLoadingContext(); 
		ModuleContext module = appContext.getModules().get(uri);
		if(module==null) {
			String baseuri = confElement.getOwnerDocument().getBaseURI();
			File f = new File(new File(new URL(baseuri).getPath()).getParent(),uri);
			module = new ModuleContext(f);
			appContext.getModules().put(uri, module);
		}
		return module.getFunction(cNames[1]);
	}
	
	private ModuleContext getModuleContext(String name) {
		String uri = confElement.getOwnerDocument().lookupNamespaceURI(name);
		ApplicationContextImpl appContext = ApplicationContextImpl.getLoadingContext(); 
		return appContext.getModules().get(uri);
		
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
		ScriptEngine engine = getEngine(con);
		con.setAutoCommit(false);
		try {
	 		Invocable invocable = (Invocable) engine;
	 		Object result = invocable.invokeFunction("rpc_func",cargs.toArray());
	 		con.commit();
			return result; 
		} catch (ScriptException e) {
			con.rollback();
			throw e;
		}
		
	}

	private ScriptEngine getEngine(Connection con) throws ScriptException {
		if(jsEngine==null) {
			ScriptEngineManager manager = new ScriptEngineManager();
			jsEngine = manager.getEngineByName("JavaScript");
			StringBuffer sfunc = new StringBuffer("function rpc_func(");
			for (int i = 0, len = arguments.size(); i < len; i++) {
				sfunc.append(arguments.get(i).getName());
				if(i<len-1) sfunc.append(',');
			}
			sfunc.append(")");
			sfunc.append(body);
			SimpleBindings bindings = new SimpleBindings();
			prepareGlobalVariables(bindings);
			jsEngine.setBindings(bindings,ScriptContext.GLOBAL_SCOPE);
			jsEngine.eval(sfunc.toString());
		}
		jsEngine.getBindings(ScriptContext.GLOBAL_SCOPE).put("connection", con);
		return jsEngine;
	}

	private void prepareGlobalVariables(SimpleBindings bindings) {
		// TODO Auto-generated method stub
		//jsEngine.getBindings(ScriptContext.GLOBAL_SCOPE).get("connection");
		bindings.put("modules", new Object() {
			public ModuleContext getModule(String name) {
				return getModuleContext(name);
			};
		});
	}

}
