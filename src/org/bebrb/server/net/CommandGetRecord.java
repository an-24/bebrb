package org.bebrb.server.net;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import org.bebrb.data.DataSource;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.View;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataSourceImpl;

import com.google.gson.Gson;

public class CommandGetRecord extends Command{
	private String sessionId;
	private Object keyValue;
	private String dtsId;

	protected CommandGetRecord() {
		super(Type.GetRecord);
	}

	public CommandGetRecord(String sessionId, String dtsId, Object keyValue) {
		this();
		this.dtsId = dtsId;
		this.keyValue = keyValue;
		this.sessionId = sessionId;
	}
	
	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		DataSource ds=null;
		String[] dtsIdParts = dtsId.split(".");
		if(dtsIdParts.length>1) {
			ReferenceBook ref = session.getAppContext().getDataSourceManager().findReference(dtsIdParts[0]);
			if(ref==null)
				throw new ExecuteException("ReferenceNotFound",dtsIdParts[0]);
			View view = ref.getViews().get(dtsIdParts[1]);
			if(view==null)
				throw new ExecuteException("ViewNotFound",dtsIdParts[1]);
			ds = view.getDatasource();
		} else {
			ds = session.getAppContext().getDataSourceManager().findDataSource(dtsId);
		}
		if(ds==null)
			throw new ExecuteException("DatasourceNotFound",dtsId);
		try(Connection con = ((DataSourceImpl)ds).getConnection(session)) {
			response.data = ((DataSourceImpl)ds).innerOpen(con, keyValue, session);
		}
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}
	
	class Response extends Command.Response {
		Map<String,Object> data;
	}

}
