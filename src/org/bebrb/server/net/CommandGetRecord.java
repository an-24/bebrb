package org.bebrb.server.net;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bebrb.reference.ReferenceBook;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.data.ReferenceBookImpl;

import com.google.gson.Gson;

public class CommandGetRecord extends Command{
	private String sessionId;
	private Object keyValue;
	private String datasourceId;
	private String refId;

	protected CommandGetRecord() {
		super(Type.GetRecord);
	}

	public CommandGetRecord(String sessionId, Object keyValue) {
		this();
		this.keyValue = keyValue;
		this.sessionId = sessionId;
	}
	
	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		
		String  sqltext = null,
				dsId = null;
		Connection con = null;
		
		if(refId!=null) {
			ReferenceBook ref = session.getAppContext().getDataSourceManager().findReference(refId);
			if(ref==null)
				throw new ExecuteException("ReferenceNotFound",refId);
			dsId = refId;
			sqltext = ((ReferenceBookImpl)ref).getGetRecordSQL();
			con = ((ReferenceBookImpl)ref).getConnection(session);
		} else
		if(datasourceId!=null) {
			DataSourceImpl ds = (DataSourceImpl) session.getAppContext().getDataSourceManager().findDataSource(datasourceId);
			if(ds==null)
				throw new ExecuteException("DatasourceNotFound",datasourceId);
			dsId = datasourceId;
			sqltext = "select * from("+ds.getSqlText()+") as A where A."+ds.getKey().getName()+"=:id";
			con = ((DataSourceImpl)ds).getConnection(session);
		} else
			throw new ExecuteException("InternalError",101003);
		
		try {
			Map<String,Object> params =  new HashMap<>();
			params.put("id", null);
			List<String> inParams = new ArrayList<>();
			String sql = DataSourceImpl.parse(dsId, sqltext, params, inParams);
			if(!inParams.contains("id"))
				throw new ExecuteException("ReqParamNotFound","id");
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setObject(1, keyValue);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				Map<String, Object> rec = new HashMap<>();
				ResultSetMetaData meta = rs.getMetaData();
				for (int j = 1, len = meta.getColumnCount(); j <= len; j++) {
					rec.put(meta.getColumnLabel(j),rs.getObject(j));
				}
				response.data = rec;
			}
			
		} finally {
			con.close();
		}
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}
	
	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	class Response extends Command.Response {
		Map<String,Object> data;
	}

}
