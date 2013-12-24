package org.bebrb.server.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
	private ScriptEngine jsEngine;
	private Element elem;
	private ApplicationContextImpl appContext;

	public RemoteFunctionImpl(Element el) throws SAXException, IOException, ParserConfigurationException {
		this.elem = el;
		appContext = ApplicationContextImpl.getLoadingContext();
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
		ModuleContext module = getModuleContext(cNames[0]);
		if(module==null) {
			String uri = elem.getOwnerDocument().lookupNamespaceURI(cNames[0]);
			String baseuri = elem.getOwnerDocument().getBaseURI();
			File f = new File(new File(new URL(baseuri).getPath()).getParent(),uri);
			module = new ModuleContext(cNames[0],uri,f);
			appContext.getModules().put(uri, module);
		}
		return module.getFunction(cNames[1]);
	}
	
	private ModuleContext getModuleContext(String name) {
		String uri = elem.getOwnerDocument().lookupNamespaceURI(name);
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
			StringBuffer sfunc = new StringBuffer();
			sfunc.append("function rpc_func(");
			for (int i = 0, len = arguments.size(); i < len; i++) {
				sfunc.append(arguments.get(i).getName());
				if(i<len-1) sfunc.append(',');
			}
			sfunc.append(")");
			sfunc.append(body);
			// utils.log(s) -> log(s)
			sfunc.append("\nvar log = function(s){utils.log(s);};");
			SimpleBindings bindings = new SimpleBindings();
			prepareGlobalVariables(bindings);
			jsEngine.setBindings(bindings,ScriptContext.GLOBAL_SCOPE);
			jsEngine.eval(sfunc.toString());
		}
		jsEngine.getBindings(ScriptContext.GLOBAL_SCOPE).put("connection", con);
		return jsEngine;
	}

	private List<Object> convertArgs(Object... args) {
		List<Object> arl = new ArrayList<>();
		for (int i = 0; i < args.length; i++) arl.add(args[i]);
		return arl;
	}

	private void prepareGlobalVariables(SimpleBindings bindings) {
		// modules.get("modulename").function("funcname").exec(a1,a2...)
		bindings.put("modules", new Modules() {
			@Override
			public Module get(final String mname) {
				final Connection mConnection = (Connection) jsEngine.getBindings(ScriptContext.GLOBAL_SCOPE).get("connection");
				return new Module() {
					private ModuleContext m = getModuleContext(mname);
					@Override
					public Function func(final String fname) {
						return new Function() {
							private RemoteFunctionImpl f = (RemoteFunctionImpl) m.getFunction(fname);
							@Override
							public Object exec(Object ... args) throws Exception {
								List<Object> arl = convertArgs(args);
								return f.exec(arl, mConnection);
							}
						};
					}
				};
			};
		});
		// data.getDataSource("dsname").insert(..) | update(...) | delete(id)
		bindings.put("data",new Data(){
			@Override
			public DataModule getDataSource(final String name) {
				final Connection mConnection = (Connection) jsEngine.getBindings(ScriptContext.GLOBAL_SCOPE).get("connection");
				final DataSourceImpl ds = (DataSourceImpl) appContext.getDataSourceManager().findDataSource(name);
				return ds==null?null:new DataModule() {

					@Override
					public Object insert(Object... args) throws Exception {
						RemoteFunctionImpl rf = (RemoteFunctionImpl) ds.getInsertFunc();
						if(rf==null) throw new ExecuteException("FuncNotFound",name+".insert");
						return rf.exec(convertArgs(args), mConnection);
					}

					@Override
					public void update(Object... args) throws Exception {
						RemoteFunctionImpl rf = (RemoteFunctionImpl) ds.getUpdateFunc();
						if(rf==null) throw new ExecuteException("FuncNotFound",name+".update");
						rf.exec(convertArgs(args), mConnection);
					}

					@Override
					public void delete(Object id) throws Exception {
						RemoteFunctionImpl rf = (RemoteFunctionImpl) ds.getDeleteFunc();
						if(rf==null) throw new ExecuteException("FuncNotFound",name+".delete");
						rf.exec(convertArgs(id), mConnection);
					}
					
				};
			}
			@Override
			public DataModule getReferenceBook(final String name) {
				final Connection mConnection = (Connection) jsEngine.getBindings(ScriptContext.GLOBAL_SCOPE).get("connection");
				final ReferenceBookImpl rb = (ReferenceBookImpl) appContext.getDataSourceManager().findReference(name);
				return rb==null?null:new DataModule() {

					@Override
					public Object insert(Object... args) throws Exception {
						RemoteFunctionImpl rf = (RemoteFunctionImpl) rb.getInsertFunc();
						if(rf==null) throw new ExecuteException("FuncNotFound",name+".insert");
						return rf.exec(convertArgs(args), mConnection);
					}

					@Override
					public void update(Object... args) throws Exception {
						RemoteFunctionImpl rf = (RemoteFunctionImpl) rb.getUpdateFunc();
						if(rf==null) throw new ExecuteException("FuncNotFound",name+".update");
						rf.exec(convertArgs(args), mConnection);
					}

					@Override
					public void delete(Object id) throws Exception {
						RemoteFunctionImpl rf = (RemoteFunctionImpl) rb.getDeleteFunc();
						if(rf==null) throw new ExecuteException("FuncNotFound",name+".delete");
						rf.exec(convertArgs(id), mConnection);
					}
					
				};
			}
		});
		// utils.log(s)
		bindings.put("utils",new JSUtils() {
			@Override
			public void log(String s) {
				ApplicationContextImpl.getLogger().log(Level.INFO, s);
			}
		}); 
		// config.get("propname")
		bindings.put("config",new JSConfig(){
			@Override
			public String get(String name) {
				return appContext.getConfig().getParam(name, null);
			}
		});
	}
	
	public interface JSConfig {
		public String get(String name);
	}
	
	public interface JSUtils {
		public void log(String s);
	}
	
	public interface Modules {
		public Module get(String name);
	}
	public interface Module {
		public Function func(String name);
	}
	public interface Function {
		public Object exec(Object ... args) throws Exception;
	}
	public interface Data {
		public DataModule getDataSource(String name);
		public DataModule getReferenceBook(String name);
	}
	public interface DataModule {
		public Object insert(Object ... args) throws Exception;
		public void update(Object ... args) throws Exception;
		public void delete(Object id) throws Exception;
	}

}
