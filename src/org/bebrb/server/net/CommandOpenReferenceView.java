package org.bebrb.server.net;

import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bebrb.data.DataPage;
import org.bebrb.data.DataSource;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData.ReferenceType;
import org.bebrb.reference.View;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataPageImpl;
import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.utils.CopyInDepth;
import org.bebrb.server.utils.NoCopy;
import org.bebrb.server.utils.ReflectUtils;

import com.google.gson.Gson;

public class CommandOpenReferenceView  extends Command {
	private String sessionId;
	private String refId;
	private String viewId;
	private Map<String,Object> params;
	private Date actualDate;
	private Date viewDate;
	private Integer folderId;


	protected CommandOpenReferenceView() {
		super(Type.OpenReferenceView);
	}
	
	public CommandOpenReferenceView(String sessionId, String refId) {
		this();
		this.sessionId = sessionId;
		this.refId = refId;
	}
	
	public CommandOpenReferenceView(String sessionId, String refId, String viewId) {
		this(sessionId,refId);
		this.viewId = viewId;
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Date getViewDate() {
		return viewDate;
	}

	public void setViewDate(Date viewDate) {
		this.viewDate = viewDate;
	}

	public Date getActualDate() {
		return actualDate;
	}

	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}

	public Integer getFolderId() {
		return folderId;
	}

	public void setFolderId(Integer folderId) {
		this.folderId = folderId;
	}

	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		ReferenceBook ref = session.getAppContext().getDataSourceManager().findReference(refId);
		if(ref==null)
			throw new ExecuteException("ReferenceNotFound",refId);
		View view = ref.getDefaultView();
		if(viewId!=null) view = ref.getViews().get(viewId);
		if(view==null)
			throw new ExecuteException("ViewNotFound",viewId);
		DataSource ds = view.getDatasource();
		//id cursor
		BigInteger cursorId = ((DataSourceImpl)ds).newCursorId();
		// execute
		List<DataPage> pages;
		try(Connection con = ((DataSourceImpl)ds).getConnection(session)) {
			if(ref.getMetaData().isHistoryAvailable()) {
				if(params==null) params = new HashMap<String, Object>();
				params.put("viewDate", viewDate==null?new Date():viewDate);
			}
			if(ref.getMetaData().getReferenceType() == ReferenceType.Hierarchy && folderId!=null) {
				if(params==null) params = new HashMap<String, Object>();
				params.put("folderId", folderId);
			}
			pages = ((DataSourceImpl)ds).innerOpen(con,params,cursorId,session);
			if(pages!=null) {
				response.pages = new ArrayList<>(pages.size());
				for (DataPage dp : pages) {
					Page page = ReflectUtils.copyFields(dp, new Page());
					page.data = ((DataPageImpl)dp).getData(true);
					response.pages.add(page);
				}
			}
		};
		//id cursor & put in cache
		if(pages!=null && ds.isLazy()) {
			session.getDatasetCache().put(cursorId, pages);
			response.cursorId = cursorId.toString();
		};
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}

	class Page {
		Integer size;
		Boolean eof;
		Boolean alive;
		@NoCopy
		List<List<Object>> data;
	}
	
	class Response extends Command.Response {
		String cursorId;
		@CopyInDepth
		List<Page> pages;
	}

}
