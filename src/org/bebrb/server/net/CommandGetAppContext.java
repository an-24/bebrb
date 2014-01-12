package org.bebrb.server.net;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bebrb.server.ApplicationContextImpl;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.utils.CopyInDepth;
import org.bebrb.server.utils.ReflectUtils;

import com.google.gson.Gson;

public class CommandGetAppContext extends Command {
	String sessionId; 

	protected CommandGetAppContext() {
		super(Type.GetAppContext);
	}

	public CommandGetAppContext(String sessionId) {
		this();
		this.sessionId = sessionId;
	}
	
	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		ApplicationContextImpl ctx = session.getAppContext();
		ReflectUtils.copyFields(ctx, response);
		filter(response);
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}
	
	private void filter(Response response) {
		// filter public data source
		List<DataSource> list = response.dataSources;
		for (int i = 0; i < list.size(); i++) {
			DataSource ds = list.get(i);
			if(!ds.published) {
				list.remove(i);
				i--;
			}
		}
	}

	public static class User {
		String loginName;
		String fullName;
		String email;
	}
	
	public static class Attribute {
		String name;
		String caption;
		org.bebrb.data.Attribute.Type type;
		Boolean key;
		Attribute foreignKey;
		Boolean visible;
		Boolean mandatory;
		Integer maxSizeChar;
	}
	
	public static class View {
		String name;
		String title;
		String dataSetId;
		Integer root;
	}
	
	public static class RMetaData {
		String referenceTitle;
		Boolean historyAvailable;
		org.bebrb.reference.ReferenceBookMetaData.ReferenceType referenceType;
		Date actualDate;
		String parentKey;
		Boolean canChoiseFolder;
	}
	
	public static class Reference {
		@CopyInDepth
		RMetaData metaData;
		String defaultView;
		@CopyInDepth
		Map<String, View> views;
	}
	
	public static class DataSource {
		String id;
		@CopyInDepth
		List<Attribute> attributes;
		String key;
		org.bebrb.data.DataSource.CacheControl cacheControl;
		Boolean published;
		Boolean lazy;
		Boolean readOnly;
		Boolean canAdd;
		Boolean canDelete;
		Boolean canEdit;
		int maxSizeDataPage;
		Date actualDate;
	}

	public class Response extends Command.Response {
		String title;
		Date lastLoginDate;
		@CopyInDepth
		User lastLoginUser;
		String version;
		@CopyInDepth
		List<Reference> references;
		@CopyInDepth
		List<DataSource> dataSources;
	}
}
