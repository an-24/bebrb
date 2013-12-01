package org.bebrb.server.net;

import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bebrb.data.DataPage;
import org.bebrb.data.DataSource;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataPageImpl;
import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.utils.CopyInDepth;
import org.bebrb.server.utils.NoCopy;
import org.bebrb.server.utils.ReflectUtils;

import com.google.gson.Gson;

public class CommandOpenDatasource extends Command {
	private String sessionId;
	private String id;
	private Map<String,Object> params;

	protected CommandOpenDatasource() {
		super(Type.OpenDatasource);
	}

	public CommandOpenDatasource(String sessionId, String id, Map<String, Object> params) {
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
		//id cursor
		BigInteger cursorId = null;
		if(ds.isLazy()) {
			byte[] code = new byte[16];
			new SecureRandom().nextBytes(code);
			cursorId = new BigInteger(1,code);
		};
		// execute
		List<DataPage> pages;
		try(Connection con = session.getConnection()) {
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
