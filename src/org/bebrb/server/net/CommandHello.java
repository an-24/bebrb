package org.bebrb.server.net;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bebrb.server.ApplicationContext;

import com.google.gson.Gson;

/**
 * Format of reply for Hello request
 * <pre>
 * {status:0,apps:[{name:&lt;name&gt;,title:&lt;title&gt;,release:&lt;release&gt;},...]}
 * </pre>
 */
public class CommandHello extends Command {

	public CommandHello() {
		super(Type.Hello);
	}

	class ApplicationInfo {
		public final String name;
		public final String title;
		public final Date release;
		
		public ApplicationInfo(String name, String title, Date release) {
			this.name = name;
			this.title = title;
			this.release = release;
		}
	}
	
	@Override
	public void solution(OutputStream out) throws WriteStreamException {
		ArrayList<ApplicationInfo> ret = new ArrayList<ApplicationInfo>();
		List<ApplicationContext> apps = ApplicationContext.getApplications();
		for (ApplicationContext ctx : apps) {
			ret.add(new ApplicationInfo(ctx.getName(),ctx.getTitle(),
					ctx.getVersion().getRelease()));
		};
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, "{status:0,apps:"+gson.toJson(ret)+"}");
	}

}
