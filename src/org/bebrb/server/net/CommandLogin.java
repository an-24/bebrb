package org.bebrb.server.net;

import java.io.OutputStream;
import java.util.ArrayList;

import org.bebrb.context.SessionContext;
import org.bebrb.context.UserAgent;
import org.bebrb.server.ApplicationContext;

import com.google.gson.Gson;

/**
 * Format of reply for Login request
 * <pre>
 * {status:0,session:{id:&lt;session id&gt;,version:&lt;application version&gt;,
 *                    datasources:[data sources list],refs:[references list]}}
 * </pre>
 * Format of data sources list
 * <pre>
 * [{id:&lt;data source id&gt;,cc:&lt;cache control type&gt;[,actual:&lt;actual date&gt;]},...]
 * </pre>
 * Format of references list
 * <pre>
 * [{id:&lt;reference id&gt;,cc:&lt;cache control type&gt;[,actual:&lt;actual date&gt;]},...]
 * </pre>
 * 
 * Cache control type: "WithinSession" or "IsModified"
 * 
 */
public class CommandLogin extends Command {

	private String app = null;
	private String user = null;
	private String pswd = null;
	private UserAgent userAgent = null;
	
	protected CommandLogin() {
		super(Type.Login);
	}
	
	public CommandLogin(String app, String user, String pswd) {
		this();
		this.userAgent = new UserAgent();
		this.app = app;
		this.user = user;
		this.pswd = pswd;
	}

	public String getApp() {
		return app;
	}

	public String getUser() {
		return user;
	}


	public String getPswd() {
		return pswd;
	}


	public UserAgent getUserAgent() {
		return userAgent;
	}
	

	@Override
	public void solution(OutputStream out) throws WriteStreamException, Exception {
		Response response = new Response();
		
		ApplicationContext ctx = new ApplicationContext(app);
		SessionContext session = ctx.login(user,pswd,userAgent);
		if(session==null)
			throw new Exception(ApplicationContext.getStrings().getString("loginError"));
		
		response.session.id = session.getId();
		response.session.version = ctx.getVersion().toString();
		response.session.ctx = ctx;
		
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}

	public class SessionInfo {
		String id;
		String version;
		transient ApplicationContext ctx;
		ArrayList<?> datasources = null;
		ArrayList<?> refs = null;
		
		public String getId() {
			return id;
		}
		
		public String getVersion() {
			return version;
		}
		
		@SuppressWarnings("rawtypes")
		public ArrayList<?> getDatasources() {
			if(datasources==null) {
				datasources = new ArrayList();
			}
			return datasources;
		}
		public ArrayList<?> getRefs() {
			return refs;
		}
	}

	public class Response {
		int status = 0;
		SessionInfo session = new SessionInfo();
		
		public SessionInfo getSession() {
			return session;
		}
	}
}
