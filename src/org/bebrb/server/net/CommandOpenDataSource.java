package org.bebrb.server.net;

import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bebrb.data.DataPage;
import org.bebrb.data.DataSource;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataPageImpl;
import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.data.DataSourceImpl.SortAttribute;
import org.bebrb.server.utils.CopyInDepth;
import org.bebrb.server.utils.NoCopy;
import org.bebrb.server.utils.ReflectUtils;

import com.google.gson.Gson;

public class CommandOpenDataSource extends Command {
	private String sessionId;
	private String id;
	private Map<String,Object> params;
	private Integer pageSize;
	private List<SortAttribute> sorting;
	private boolean firstPageBonus = true;

	protected CommandOpenDataSource() {
		super(Type.OpenDatasource);
	}

	public CommandOpenDataSource(String sessionId, String id, Map<String, Object> params) {
		this();
		this.sessionId = sessionId;
		this.id = id;
		this.params = params;
	}

	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		DataSource ds = session.getAppContext().getDataSourceManager().findDataSource(id);
		if(ds==null)
			throw new ExecuteException("DatasourceNotFound",id);
		if(!ds.isPublished())
			throw new ExecuteException("DatasourceNotPublished",id);
		boolean lazy = ds.isLazy();
		//id cursor
		BigInteger cursorId = ((DataSourceImpl)ds).newCursorId();
		// execute
		DataSourceImpl dsimpl = ((DataSourceImpl)ds);
		List<DataPage> pages;
		try(Connection con = dsimpl.getConnection(session)) {
			if(pageSize!=null) dsimpl.setMaxSizeDataPage(pageSize);
			if(sorting!=null) dsimpl.setSortedAttributes(sorting);
			pages = dsimpl.innerOpen(con,params,cursorId,session);
			response.recordCount = dsimpl.getRecordCount();
			if(pages!=null) {
				response.pages = new ArrayList<>(pages.size());
				for (DataPage dp : pages) {
					Page page = ReflectUtils.copyFields(dp, new Page());
					page.data = ((DataPageImpl)dp).getData(lazy);
					response.pages.add(page);
				}
				// first page bonus!
				if(lazy && firstPageBonus) {
					DataPage dp = pages.get(0); 
					Page page = response.pages.get(0);
					page.data = ((DataPageImpl)dp).getData(false);
					page.alive = dp.isAlive();
					page.eof = dp.isEof();
					page.size = dp.getSize();
				}
			}
		};
		//id cursor & put in cache
		if(lazy) {
			session.getDatasetCache().put(cursorId, pages);
			response.cursorId = cursorId.toString();
		};
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public List<SortAttribute> getSorting() {
		return sorting;
	}

	public void setSorting(SortAttribute ... attributes) {
		List<SortAttribute> attrs =  new ArrayList<>();
		for (SortAttribute a : attributes) 
			attrs.add(a);
		setSorting(attrs);
	}

	public void setSorting(List<SortAttribute> sorting) {
		this.sorting = sorting;
	}
	
	public static class Page {
		Integer size;
		Boolean eof;
		Boolean alive;
		@NoCopy
		List<List<Object>> data;
		
		public Integer getSize() {
			return size;
		}
		public Boolean getEof() {
			return eof;
		}
		public Boolean getAlive() {
			return alive;
		}
		public List<List<Object>> getData() {
			return data;
		}
	}
	
	public static class Response extends Command.Response {
		String cursorId;
		@CopyInDepth
		List<Page> pages;
		Integer recordCount;
		
		public BigInteger getCursorId() {
			return cursorId==null?null:new BigInteger(cursorId);
		}
		public List<Page> getPages() {
			return pages;
		}
		
		public Integer getRecordCount() {
			return recordCount;
		}
	}

}
