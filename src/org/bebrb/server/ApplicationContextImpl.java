package org.bebrb.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.bebrb.context.ApplicationContext;
import org.bebrb.context.Config;
import org.bebrb.context.SessionContext;
import org.bebrb.context.UserAgent;
import org.bebrb.data.DataSource;
import org.bebrb.forms.Action;
import org.bebrb.forms.Form;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.bebrb.user.User;
import org.bebrb.utils.UTF8ResourceBundleControl;
import org.bebrb.utils.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import biz.gelicon.dbcp.ConnectionPool;
import biz.gelicon.dbcp.DriverManagerConnectionFactory;

public class ApplicationContextImpl implements ApplicationContext {
	private static ApplicationContextImpl loadedApp;
	
	private String name;
	private Version version = null;
	private String title;
	private Locale locale;

	private static Logger log = Logger.getLogger("bebrb");
	private DataSources datasources;
	private Map<String, ModuleContext> modules = new HashMap<String, ModuleContext>();
	private List<String> dataModules = new ArrayList<String>();

	private String dbDriverName;
	private String dbUrl;
	private String dbSysUser;
	private String dbSysPswd;
	private Properties dbParams = new Properties();

	private int dbPoolMin = 1;
	private int dbPoolMax = 4;
	private int maxRequestThreads = 6;

	private ConnectionPool pool;

	private boolean identCaseSensitive;
	private Map<String, String> config = new HashMap<>();
	private Properties stringTable;

	private String home = "";


	private static ResourceBundle strings = null;
	
	public ApplicationContextImpl(String name, Version version) {
		this.name = name;
		this.version = version;
		load();
	}

	public ApplicationContextImpl(String name) {
		// maybe starting '/' 
		if(name.startsWith("/")) name = name.substring(1);
		
		this.name = name;
		String[] anames = name.split("/");
		if(anames.length>1) {
			this.name = anames[0];
			this.version = new Version(anames[1]);
		}
		load();
	}
	
	public List<String> getDataModules() {
		return dataModules;
	}

	public static Logger getLogger() {
		return log;
	}
	
	public DataSources getDataSourceManager() {
		return datasources;
	}

	public String getTitle() {
		return title;
	}
	
	private synchronized void load() {
		try {
			home = getHome();
			
			log.info("start app["+name+"-"+(version==null?"default":version)+"] loading process...");
			loadedApp = this;
			loadApplication();
			strings = ResourceBundle.getBundle("org/bebrb/resources/strings/messages", locale, new UTF8ResourceBundleControl()); 
			loadDataSources();
			afterLoad();
			loadedApp = null;
			log.info("app loading process [ok]");
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error config", e);
		}
	}

	private void loadApplication() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder builder = createXMLBuider("/org/bebrb/resources/shema/app.xsd");
		Document doc = builder.parse(getBasePath()+"app.xml");
		Element root = doc.getDocumentElement();
		Element inf = XMLUtils.findChild(root, "info");
		title = inf.getAttribute("title");
		String langTag = inf.getAttribute("lang");
		if(langTag.isEmpty()) locale = Locale.getDefault();
						else  locale = new Locale(langTag);
		if(version==null) {
			String sver = XMLUtils.findChild(root, "versions").getAttribute("default");
			version = new Version(sver);
		}
		Element el = XMLUtils.findChild(XMLUtils.findChild(root, "versions"),"ver", "number",version.toString());
		if(el==null)
			throw new SAXException("Version "+version+" not found");
		
		Date release = DatatypeConverter.parseDate(el.getAttribute("release")).getTime();
		version.setRelease(release);
		
		Element confel = XMLUtils.findChild(el, "config");
		if(confel!=null)
			XMLUtils.enumChildren(confel,new NotifyElement() {
				@Override
				public boolean notify(Element e) {
					if(e.getNodeName().equals("option")) {
						config.put(e.getAttribute("name"), e.getAttribute("value"));
					}
					return false;
				}
			});
		
		Element dbel = XMLUtils.findChild(el, "database");
		dbDriverName = dbel.getAttribute("driver");
		dbUrl = dbel.getAttribute("url");
		identCaseSensitive = new Boolean(dbel.getAttribute("ident-case-sensitive"));
		dbSysUser = dbel.getAttribute("user");
		if(dbSysUser.isEmpty()) dbSysUser = null;
		dbSysPswd = dbel.getAttribute("password");
		if(dbSysPswd.isEmpty()) dbSysPswd = null;
		XMLUtils.enumChildren(XMLUtils.findChild(dbel,"params"),new NotifyElement() {
			@Override
			public boolean notify(Element e) {
				if(e.getNodeName().equals("param")) {
					dbParams.put(e.getAttribute("name"), e.getAttribute("value"));
				}
				return false;
			}
		});
		
		XMLUtils.enumChildren(XMLUtils.findChild(el, "data"), new NotifyElement() {
			@Override
			public boolean notify(Element e) {
				dataModules.add(e.getAttribute("name"));
				return false;
			}
		});

		Element perfel = XMLUtils.findChild(root, "performance");
		if(perfel!=null) {
			Element poolel = XMLUtils.findChild(perfel, "database-pool");
			if(poolel!=null) {
				if(poolel.getAttributeNode("max")!=null)
					dbPoolMax = Integer.valueOf(poolel.getAttribute("max"));
				if(poolel.getAttributeNode("min")!=null)
					dbPoolMin = Integer.valueOf(poolel.getAttribute("min"));
			}
			Element threadsel = XMLUtils.findChild(perfel, "threads");
			if(threadsel!=null)
				maxRequestThreads = Integer.valueOf(poolel.getAttribute("max"));
		}
		loadResourceStrings();
	}
	
	public static List<ApplicationContextImpl> getApplications() throws IOException {
		File[] files = new File(getHome()+"applications").listFiles();
		if(files==null)
			throw new IOException("Path not found "+new File("applications").getAbsolutePath());
		List<ApplicationContextImpl> lactx = new ArrayList<ApplicationContextImpl>();
		for (File file : files) 
		if(file.isDirectory()) {
			ApplicationContextImpl ctx = new ApplicationContextImpl(file.getName());
			lactx.add(ctx);
		}
		return lactx;
	}

	public Map<String, ModuleContext> getModules() {
		return modules;
	}

	
	private void afterLoad() throws Exception {
		datasources.afterLoad();
	}

	public String getVersionBasePath(){
		return home+"applications"+File.separatorChar+getName()+
				File.separator+getName()+"-"+getVersion()+File.separator;
	}

	public String getBasePath(){
		return home+"applications"+File.separatorChar+getName()+
				File.separator;
	}
	
	public static String getHome() {
		return Server.getHome();
	}
	
	private void loadDataSources() throws IOException, SAXException, ParserConfigurationException {
		log.info("+start data modules loading process...");
		datasources = new DataSources(this);
		log.info("-data modules loading process [ok]");
	}

	public String getName() {
		return name;
	}


	public Version getVersion() {
		return version;
	}

	public static DocumentBuilder createXMLBuider(String xsdPath) throws IOException, SAXException, ParserConfigurationException {
		URL xsd = System.class.getResource(xsdPath);
		if(xsd==null)
			throw new IOException("XSD shema not found "+xsdPath);
		Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsd);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setSchema(schema);
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new ErrorHandler() {
			
			@Override
			public void warning(SAXParseException exception) throws SAXException {
				ApplicationContextImpl.getLogger().warning(exception.getMessage());
			}
			
			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				throw new SAXException(exception);
			}
			
			@Override
			public void error(SAXParseException exception) throws SAXException {
				throw new SAXException(exception);
			}
		});
		return builder;
	}

	public static ApplicationContextImpl getLoadingContext() {
		return loadedApp;
	}

	public SessionContext login(String user, String pswd, UserAgent userAgent) {
		User puser = null;
		String hashPswd = null;
		// TODO find user in user store
		
		// System user
		if(puser==null) {
			final String sysuser = System.getenv("BEBRB-USER");
			if(sysuser!=null && sysuser.equals(user)) {
				puser = new User(){
					@Override
					public String getLoginName() {
						return sysuser;
					}
					@Override
					public String getFullName() {
						return sysuser;
					}
					@Override
					public String getEmail() {
						return null;
					}
				};
				hashPswd = System.getenv("BEBRB-PSWD");
			}
		}
		
		if(puser==null || hashPswd==null) return null;
		
		try {
			if(hashPassword(pswd).equals(hashPswd))
				return new SessionContextImpl(this, new UserContextImpl(puser,userAgent));
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error login", e);
		}
		return null;
	}

	public void logout(SessionContext sessionContext) {
		sessionContext.logout();
	}

	public static ResourceBundle getStrings() {
		return strings;
	}

	
	private String hashPassword(String pswd) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.update(pswd.getBytes(), 0, pswd.length());
		pswd = new BigInteger(1, digest.digest()).toString(16);
		return pswd;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public Config getConfig() {
		return new Config(){
			@Override
			public String getParam(String name, String def) {
				String v = config.get(name);
				return v==null?def:v;
			}

			@Override
			public void setParam(String name, String value) {
				config.put(name, value);
			}
		};
	}

	@Override
	public String getLocation() {
		// no implementation on server-side
		return null;
	}

	@Override
	public Date getLastLoginDate() {
		// TODO after implementation statistics db
		return null;
	}

	@Override
	public User getLastLoginUser() {
		// TODO after implementation statistics db
		return null;
	}

	@Override
	public Set<User> getActiveUsers() {
		// TODO after implementation statistics db
		return null;
	}

	@Override
	public SessionContext getActiveSession() {
		// no implementation on server-side
		return null;
	}

	@Override
	public Form getActiveForm() {
		// no implementation on server-side
		return null;
	}

	@Override
	public Form getMainForm() {
		// no implementation on server-side
		return null;
	}

	@Override
	public List<ReferenceBook> getReferences() {
		return datasources.getRefs();
	}

	@Override
	public List<DataSource> getDataSources() {
		return datasources.getDatasources();
	}
	
	@Override
	public Set<Action> getActions() {
		// no implementation on server-side
		return null;
	}

	public ConnectionPool getDatabasePool() throws ClassNotFoundException {
		if(pool==null && dbDriverName!=null) {
			Properties props = new Properties(dbParams);
			if(dbSysUser!=null) props.put("user", dbSysUser);
			if(dbSysPswd!=null) props.put("password", dbSysPswd);
			pool = new ConnectionPool(new DriverManagerConnectionFactory(dbDriverName, dbUrl, props), 
					dbPoolMin, dbPoolMax);
		}
		return pool;
	}

	public boolean isIdentCaseSensitive() {
		return identCaseSensitive;
	}
	
	@SuppressWarnings("serial")
	private void loadResourceStrings() throws FileNotFoundException, IOException {
		String path = getVersionBasePath()+File.separatorChar+"resources"+
					  File.separatorChar+"strings";
		File f = new File(path+File.separatorChar+"string_table_"+locale.getLanguage()+".properties");
		if(f.exists()) {
			stringTable = new Properties() {
				public String getProperty(String key) {
					try {
						String val = super.getProperty(key); 
						return new String(val.getBytes("ISO-8859-1"), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						log.log(Level.SEVERE, "Error config", e);
					};
					return null;
				};
			};
			stringTable.load(new FileInputStream(f));
		}
	}
	
	public String getResourceString(String key) {
		return stringTable==null?null:stringTable.getProperty(key);
	}
}
