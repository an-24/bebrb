package org.bebrb.server.data;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bebrb.data.Attribute;
import org.bebrb.data.DataPage;
import org.bebrb.data.Record;
import org.bebrb.server.ApplicationContextImpl;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.net.ExecuteException;

public class DataPageImpl implements DataPage {

	private boolean alive = false;
	private List<List<Object>> records;
	private DataSourceImpl ds;
	private boolean eof;
	private int count = 0;
	private ResultSet rs;
	private BigInteger cursorId;
	private SessionContextImpl session;
	private int maxCount;

	public DataPageImpl(BigInteger cursorId, SessionContextImpl session,
			ResultSet rs, DataSourceImpl ds, boolean lazy, boolean eof)
			throws Exception {
		this.session = session;
		this.cursorId = cursorId;
		this.rs = rs;
		this.ds = ds;
		this.eof = eof;
		this.maxCount = ds.getMaxSizeDataPage();
		records = new ArrayList<>(maxCount);
		if (!lazy) fetch();
		alive = !lazy;
	}

	private void fetch() throws SQLException, ExecuteException {
		if(alive) return;
		// fetch prev data pages
		if(cursorId!=null) {
			List<DataPage> allPages = session.getDatasetCache().get(cursorId);
			if(allPages!=null) // when first page
			for (int i = 0; i < allPages.size(); i++) {
				DataPageImpl dp = (DataPageImpl) allPages.get(i);
				if(dp==this) break;
				dp.fetch();
			}
		}
		// map fields
		List<Attribute> attrs = ds.getAttributes();
		int fieldcount = attrs.size();
		int[] map = new int[fieldcount];
		ResultSetMetaData rsmd = rs.getMetaData();
		Map<String,Integer> fieldmap = new HashMap<>();
		for (int i = 0, len = rsmd.getColumnCount(); i < len; i++) {
			String l = rsmd.getColumnLabel(i+1);
			fieldmap.put(l==null?rsmd.getColumnName(i+1):l, i+1);
		}
		for (int i = 0; i < map.length; i++) {
			Integer idx = fieldmap.get(attrs.get(i).getName());
			if(idx==null)
				throw new ExecuteException("FieldNotFound",attrs.get(i).getName());
			map[i] = idx; 
		}
		// fetching
		while(count<maxCount && rs.next()) {
			records.add(getValuesRecord(fieldcount, map));
			count++;
		}
		alive = true;
		// close result set
		if (ds.isLazy() && eof) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				ApplicationContextImpl.getLogger().info(
						"Resultset close with error: " + e.getMessage());
			}
		}
	}

	@Override
	public int getSize() {
		return count;
	}

	@Override
	public List<Record> getRecords() throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public boolean isEof() throws Exception {
		return eof;
	}

	@Override
	public boolean isAlive() {
		return alive;
	}
	
	private List<Object> getValuesRecord(int fieldcount, int[] map) throws SQLException {
		ArrayList<Object> values = new ArrayList<>(fieldcount);
		for (int i = 0; i < fieldcount; i++) {
			values.add(rs.getObject(map[i]));
		}
		return values;
	}
	

	private void notSupportedOnServer() {
		throw new RuntimeException("Not supported on server-side");
	}

	public List<List<Object>> getData(boolean noFetching) throws Exception {
		if (!alive && !noFetching) fetch();
		return records;
	}
}
