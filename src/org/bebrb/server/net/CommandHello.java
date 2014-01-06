package org.bebrb.server.net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.bebrb.server.ApplicationContextImpl;

import com.google.gson.Gson;

/**
 * 
 * Format of reply for Hello request 
 * <pre>
 * {status:0,apps:[{name:&lt;name&gt;,title:&lt;title&gt;,release:&lt;release&gt;},...]}
 * </pre>
 */
public class CommandHello extends Command {

	public CommandHello() {
		super(Type.Hello);
	}
	
	@Override
	public void solution(OutputStream out) throws WriteStreamException, IOException {
		Response response = new Response();
		List<ApplicationContextImpl> apps = ApplicationContextImpl.getApplications();
		for (ApplicationContextImpl ctx : apps) {
			response.apps.add(new AppInfo(ctx.getName(),ctx.getTitle(),
					ctx.getVersion().getRelease(),ctx.getLocale()));
		};
		
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}
	
	public class AppInfo {
		String name;
		String title;
		Date release;
		String lang;
		
		public AppInfo(String name, String title, Date release, Locale locale) {
			this.name = name;
			this.title = title;
			this.release = release;
			this.lang = locale.getLanguage();
		}
		
		public String getName() {
			return name;
		}

		public String getTitle() {
			return title;
		}

		public Date getRelease() {
			return release;
		}

		public String getLang() {
			return lang;
		}
	}

	public class Response extends Command.Response{
		ArrayList<AppInfo> apps = new ArrayList<AppInfo>();
		
		public ArrayList<AppInfo> getApps() {
			return apps;
		}
	}
}
