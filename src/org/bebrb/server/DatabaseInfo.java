package org.bebrb.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;

public class DatabaseInfo {

	private String dbDriverName;
	private String dbUrl;
	private String dbSysUser;
	private String dbSysPswd;
	private Properties dbParams = new Properties();
	private boolean identCaseSensitive;

	public DatabaseInfo(Element dbel) {
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
	}
	
	public Connection connect() throws SQLException {
		Properties props = new Properties(dbParams);
		if(dbSysUser!=null) props.put("user", dbSysUser);
		if(dbSysPswd!=null) props.put("password", dbSysPswd);
		return DriverManager.getConnection(dbUrl,dbParams);
	}

	public boolean isIdentCaseSensitive() {
		return identCaseSensitive;
	}
	
}
