package org.bebrb.server.net;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.bebrb.data.DataPage;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataPageImpl;
import org.bebrb.server.utils.CopyInDepth;
import org.bebrb.server.utils.NoCopy;
import org.bebrb.server.utils.ReflectUtils;

import com.google.gson.Gson;

public class CommandGetRecords extends Command {
	private String sessionId;
	private BigInteger cursorId;
	private int startPage;
	private int finishPage;

	public CommandGetRecords() {
		super(Type.GetRecords);
	}

	public CommandGetRecords(String sessionId, BigInteger cursorId,
			int startPage, int finishPage) {
		this();
		this.sessionId = sessionId;
		this.cursorId = cursorId;
		this.startPage = startPage;
		this.finishPage = finishPage;
	}



	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		
		List<DataPage> pages = session.getDatasetCache().get(cursorId);
		if(pages!=null) {
			if(startPage<0) startPage = 0;
			if(finishPage<0) finishPage = pages.size()-1; 
			if(finishPage>=pages.size()) finishPage = pages.size()-1; 
			
			response.pages = new ArrayList<>(finishPage-startPage+1);
			// loop for start to finish
			for (int i = startPage; i <= finishPage; i++) {
				DataPageImpl dp = (DataPageImpl) pages.get(i);
				CommandOpenDataSource.Page page = ReflectUtils.copyFields(dp, new CommandOpenDataSource.Page());
				page.data = ((DataPageImpl)dp).getData(false);
				page.alive = dp.isAlive();
				page.eof = dp.isEof();
				page.size = dp.getSize();
				response.pages.add(page);
			}
		}
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}

	class Response extends Command.Response {
		@CopyInDepth
		List<CommandOpenDataSource.Page> pages;
	}
}
