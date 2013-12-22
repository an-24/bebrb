package org.bebrb.server.net;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;

import org.bebrb.data.RemoteFunction;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.data.ReferenceBookImpl;
import org.bebrb.server.data.RemoteFunctionImpl;

import com.google.gson.Gson;

public class CommandCall extends Command {

	protected String sessionId;
	protected String referenceBookId;
	protected String datasourceId;
	protected List<Object> args;

	protected CommandCall(Type t) {
		super(t);
	}

	protected CommandCall(Type t, String sessionId) {
		this(t);
		this.sessionId = sessionId;
	}
	
	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		ReferenceBookImpl ref = null;
		DataSourceImpl ds = null;
		RemoteFunctionImpl func = null;
		if(referenceBookId!=null) {
			ref = (ReferenceBookImpl) session.getAppContext().getDataSourceManager().findReference(referenceBookId);
			if(ref==null)
				throw new ExecuteException("ReferenceNotFound",referenceBookId);
			func = (RemoteFunctionImpl) getReferenceBookFunc(ref);
		} else
		if(datasourceId!=null) {
			ds = (DataSourceImpl) session.getAppContext().getDataSourceManager().findDataSource(datasourceId);
			if(ds==null)
				throw new ExecuteException("DatasourceNotFound",datasourceId);
			func = (RemoteFunctionImpl) getDataSourceFunc(ds);
		}	
		if(ds==null && ref==null || func==null)
			throw new ExecuteException("InternalError",112008);
		
		try(Connection con = ds!=null?((DataSourceImpl)ds).getConnection(session):
									  ((ReferenceBookImpl)ref).getConnection(session)) {
			response.returnValue = func.exec(args,con);
		}
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}

	@SuppressWarnings("incomplete-switch")
	private RemoteFunction getDataSourceFunc(DataSourceImpl ds) throws ExecuteException {
		RemoteFunction func = null;
		switch (type) {
		case Insert:
			func = ds.getInsertFunc();
			break;
		case Update:
			func = ds.getUpdateFunc();
			break;
		case Delete:
			func = ds.getDeleteFunc();
			break;
		}
		if(func==null)
			throw new ExecuteException("FuncNotFound",ds.getId()+"."+type.name());
		return func;
	}

	@SuppressWarnings("incomplete-switch")
	private RemoteFunction getReferenceBookFunc(ReferenceBookImpl ref) throws ExecuteException {
		RemoteFunction func = null;
		switch (type) {
		case Insert:
			func = ref.getInsertFunc();
			break;
		case Update:
			func = ref.getUpdateFunc();
			break;
		case Delete:
			func = ref.getDeleteFunc();
			break;
		}
		if(func==null)
			throw new ExecuteException("FuncNotFound",ref.getMetaData().getId()+"."+type.name());
		return func;
	}


	public String getReferenceBookId() {
		return referenceBookId;
	}

	public void setReferenceBookId(String referenceBookId) {
		this.referenceBookId = referenceBookId;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public List<Object> getArgs() {
		return args;
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}
	
	class Response extends Command.Response {
		Object returnValue;
	}
}
