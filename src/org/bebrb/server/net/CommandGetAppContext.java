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
		
		public String getLoginName() {
			return loginName;
		}
		public String getFullName() {
			return fullName;
		}
		public String getEmail() {
			return email;
		}
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
		
		public String getName() {
			return name;
		}
		public String getCaption() {
			return caption;
		}
		public org.bebrb.data.Attribute.Type getType() {
			return type;
		}
		public Boolean getKey() {
			return key;
		}
		public Attribute getForeignKey() {
			return foreignKey;
		}
		public Boolean getVisible() {
			return visible;
		}
		public Boolean getMandatory() {
			return mandatory;
		}
		public Integer getMaxSizeChar() {
			return maxSizeChar;
		}
	}
	
	public static class View {
		String name;
		String title;
		String dataSetId;
		Integer root;
		
		public String getName() {
			return name;
		}
		public String getTitle() {
			return title;
		}
		public String getDataSetId() {
			return dataSetId;
		}
		public Integer getRoot() {
			return root;
		}
	}
	
	public static class RMetaData {
		String referenceTitle;
		Boolean historyAvailable;
		org.bebrb.reference.ReferenceBookMetaData.ReferenceType referenceType;
		Date actualDate;
		String parentKey;
		Boolean canChoiseFolder;
		
		public String getReferenceTitle() {
			return referenceTitle;
		}
		public Boolean getHistoryAvailable() {
			return historyAvailable;
		}
		public org.bebrb.reference.ReferenceBookMetaData.ReferenceType getReferenceType() {
			return referenceType;
		}
		public Date getActualDate() {
			return actualDate;
		}
		public String getParentKey() {
			return parentKey;
		}
		public Boolean getCanChoiseFolder() {
			return canChoiseFolder;
		}
	}
	
	public static class Reference {
		@CopyInDepth
		RMetaData metaData;
		String defaultView;
		@CopyInDepth
		Map<String, View> views;
		
		public RMetaData getMetaData() {
			return metaData;
		}
		public String getDefaultView() {
			return defaultView;
		}
		public Map<String, View> getViews() {
			return views;
		}
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
		
		public String getId() {
			return id;
		}
		public List<Attribute> getAttributes() {
			return attributes;
		}
		public String getKey() {
			return key;
		}
		public org.bebrb.data.DataSource.CacheControl getCacheControl() {
			return cacheControl;
		}
		public Boolean getPublished() {
			return published;
		}
		public Boolean getLazy() {
			return lazy;
		}
		public Boolean getReadOnly() {
			return readOnly;
		}
		public Boolean getCanAdd() {
			return canAdd;
		}
		public Boolean getCanDelete() {
			return canDelete;
		}
		public Boolean getCanEdit() {
			return canEdit;
		}
		public int getMaxSizeDataPage() {
			return maxSizeDataPage;
		}
		public Date getActualDate() {
			return actualDate;
		}
	}

	public class Response extends Command.Response {
		String name;
		String title;
		Date lastLoginDate;
		@CopyInDepth
		User lastLoginUser;
		String version;
		@CopyInDepth
		List<Reference> references;
		@CopyInDepth
		List<DataSource> dataSources;
		
		public String getName() {
			return name;
		}
		public String getTitle() {
			return title;
		}
		public Date getLastLoginDate() {
			return lastLoginDate;
		}
		public User getLastLoginUser() {
			return lastLoginUser;
		}
		public String getVersion() {
			return version;
		}
		public List<Reference> getReferences() {
			return references;
		}
		public List<DataSource> getDataSources() {
			return dataSources;
		}
	}
}
