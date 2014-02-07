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
import org.bebrb.server.data.DataSourceImpl.SortAttribute;
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
	private Integer pageSize;
	private List<SortAttribute> sorting;
	private boolean firstPageBonus = true;


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

	public CommandOpenReferenceView(String sessionId, String refId, String viewId, Map<String, Object> params) {
		this(sessionId,refId, viewId);
		this.params = params;
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
		CommandOpenDataSource.Response response = new CommandOpenDataSource.Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		ReferenceBook ref = session.getAppContext().getDataSourceManager().findReference(refId);
		if(ref==null)
			throw new ExecuteException("ReferenceNotFound",refId);
		View view = ref.getDefaultView();
		if(viewId!=null) view = ref.getViews().get(viewId);
		if(view==null)
			throw new ExecuteException("ViewNotFound",viewId);
		DataSource ds = view.getDataSource();
		boolean lazy = ds.isLazy();
		DataSourceImpl dsimpl = ((DataSourceImpl)ds);
		//id cursor
		BigInteger cursorId = dsimpl.newCursorId();
		// execute
		List<DataPage> pages;
		try(Connection con = dsimpl.getConnection(session)) {
			
			dsimpl.setReferenceBookView(view);
					
			if(pageSize!=null) dsimpl.setMaxSizeDataPage(pageSize);
			if(sorting!=null) dsimpl.setSortedAttributes(sorting);
			
			if(ref.getMetaData().isHistoryAvailable()) {
				getParams().put("viewDate", viewDate==null?new Date():viewDate);
			}
			if(ref.getMetaData().getReferenceType() == ReferenceType.Hierarchy) {
				if(folderId==null) folderId = ReferenceBook.MAIN_ROOT_ID;
				getParams().put("folderId", folderId);
			}
			pages = dsimpl.innerOpen(con,params,cursorId,session);
			response.recordCount = dsimpl.getRecordCount();
			
			if(pages!=null) {
				response.pages = new ArrayList<>(pages.size());
				for (DataPage dp : pages) {
					CommandOpenDataSource.Page page = ReflectUtils.copyFields(dp, new CommandOpenDataSource.Page());
					page.data = ((DataPageImpl)dp).getData(true);
					response.pages.add(page);
				}
				// first page bonus!
				if(lazy && firstPageBonus) {
					DataPage dp = pages.get(0); 
					CommandOpenDataSource.Page page = response.pages.get(0);
					page.data = ((DataPageImpl)dp).getData(false);
					page.alive = dp.isAlive();
					page.eof = dp.isEof();
					page.size = dp.getSize();
				}
			}
		};
		//id cursor & put in cache
		if(pages!=null && lazy) {
			session.getDatasetCache().put(cursorId, pages);
			response.cursorId = cursorId.toString();
		};
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}

	private Map<String, Object> getParams() {
		if(params==null) params = new HashMap<String, Object>();
		return params;
	}

	public void setPageSize(int maxSizeDataPage) {
		pageSize = maxSizeDataPage;
	}

	public void setSorting(List<SortAttribute> sorting) {
		this.sorting = sorting;
	}
	
}
