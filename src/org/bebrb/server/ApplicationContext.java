package org.bebrb.server;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ApplicationContext {
	private String name;
	private Version version;
	private Logger log = Logger.getLogger("bebrb");
	
	private DataSourcesContext datasources; 
	
	public ApplicationContext(String name, Version version) {
		this.name = name;
		this.version = version;
		try {
			loadDataSources();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error config", e);
		}
	}
	
	public String getBasePath(){
		return "applications"+File.separatorChar+getName()+
				File.separator+getName()+"-"+getVersion()+File.separator;
	}

	private void loadDataSources() throws IOException, SAXException, ParserConfigurationException {
		log.info("start datasources loading process...");
		datasources = new DataSourcesContext(this);
		log.info("datasources loading process [ok]");
	}

	public String getName() {
		return name;
	}


	public Version getVersion() {
		return version;
	}

	public DataSourcesContext getDatasources() {
		return datasources;
	}


	public static class Version {
		public final int major;
		public final int minor;
		public final int build;

		public Version(int major, int minor, int build) {
			this.major = major;
			this.minor = minor;
			this.build = build;
		}
		public Version(String v) {
			String[] vers = v.split("-");
			major = new Integer(vers[0]);
			minor = new Integer(vers[1]);
			build = new Integer(vers[2]);
		}
		
		public String toString() {
			return major+"-"+minor+"-"+build;
		}
	}



}
