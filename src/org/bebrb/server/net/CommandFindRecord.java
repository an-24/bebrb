package org.bebrb.server.net;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.bebrb.data.DataPage;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataPageImpl;
import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.utils.CopyInDepth;
import org.bebrb.server.utils.DBUtils;
import org.bebrb.server.utils.NoCopy;
import org.bebrb.server.utils.ReflectUtils;

import com.google.gson.Gson;

public class CommandFindRecord extends Command {
	private String sessionId;
	private BigInteger cursorId;
	private String key;
	private int startPage = 0;
	

	public CommandFindRecord() {
		super(Type.FindRecord);
	}
	
	public CommandFindRecord(String sessionId, BigInteger cursorId,
			int startPage, String keyValue) {
		this();
		this.sessionId = sessionId;
		this.cursorId = cursorId;
		this.startPage = startPage;
		this.key = keyValue;
	}
	

	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		Response response = new Response();
		SessionContextImpl session = (SessionContextImpl) SessionContextImpl.loadSession(sessionId);
		List<DataPage> pages = session.getDatasetCache().get(cursorId);
		if(pages!=null && pages.size()>0) {
			//key attr index
			DataSourceImpl ds = ((DataPageImpl)pages.get(0)).getDataSource();
			int idxKey = ds.getAttributes().indexOf(ds.getKey());
			Object keyValue = DBUtils.cast(key,ds.getKey().getType());

			response.rowIndex = -1;
			int finishPage = pages.size()-1; 
			response.pages = new ArrayList<>(finishPage-startPage+1);
			// loop for start to finish
			for (int i = startPage; i <= finishPage; i++) {
				DataPageImpl dp = (DataPageImpl) pages.get(i);
				Page page = ReflectUtils.copyFields(dp, new Page());
				page.data = ((DataPageImpl)dp).getData(false);
				page.alive = dp.isAlive();
				page.eof = dp.isEof();
				page.size = dp.getSize();
				response.pages.add(page);
				// find record
				int row = 0;
				for (List<?> r : page.data) {
					Object fldValue = r.get(idxKey);
					if(fldValue.equals(keyValue)) {
						response.rowIndex = row;
						break;
					}
					row++;
				}
				if(response.rowIndex>=0) break;
			}
		}
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
		public int rowIndex;
		@CopyInDepth
		List<Page> pages;
	}
}
