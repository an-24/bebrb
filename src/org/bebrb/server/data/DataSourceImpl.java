package org.bebrb.server.data;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.bind.DatatypeConverter;

import org.bebrb.data.Attribute;
import org.bebrb.data.DataPage;
import org.bebrb.data.DataSource;
import org.bebrb.data.OnValidate;
import org.bebrb.data.Record;
import org.bebrb.data.RemoteFunction;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.View;
import org.bebrb.server.DataSources;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.net.ExecuteException;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DataSourceImpl implements DataSource {
	private String id;
	private boolean lazy;
	private CacheControl cc;
	private List<Attribute> attrs = new ArrayList<Attribute>();
	private Attribute keyAttribute = null;
	private RemoteFunction insRPC = null;
	private RemoteFunction updRPC = null;
	private RemoteFunction delRPC = null;
	private int sizeDataPage = DEFAULT_PAGE_MAXSIZE;

	private String sqlText = null;
	// TODO from cache
	private Date actualCacheDate = null;
	
	private PreparedStatement statement;
	private PreparedStatement statementCount;
	private String inSQL;
	private List<String> inParams = new ArrayList<>();
	private DatabaseInfo dbinf = null;

	public DataSourceImpl(Element el, DataSources dscontext) throws Exception {
		id = el.getAttribute("id");
		lazy = Boolean.parseBoolean(el.getAttribute("lazy"));
		cc = DataSource.CacheControl.valueOf(el.getAttribute("cache-control"));
		if (cc == DataSource.CacheControl.IsModified)
			if (el.getAttribute("actualDate").isEmpty())
				actualCacheDate = new Date();
			else
				actualCacheDate = DatatypeConverter.parseDate(
						el.getAttribute("actualDate")).getTime();
		Element refref = XMLUtils.findChild(el, "reference");
		if (refref != null) {
			if (refref.hasChildNodes())
				throw new SAXException(
						"Datasource "
								+ id
								+ " is not empty body. Required or not an empty attribute \"reference\" or non-empty body datasource");
			ReferenceBook rbook = dscontext.findReference(refref
					.getAttribute("id"));
			if (rbook == null)
				throw new SAXException("Reference book["
						+ refref.getAttribute("id") + "] not found");
			View rbview = rbook.getViews().get(refref.getAttribute("view"));
			if (rbview == null)
				throw new SAXException("View[" + refref.getAttribute("view")
						+ "] in reference book[" + refref.getAttribute("id")
						+ "] not found");
			attrs.addAll(rbook.getMetaData().getAttributes());
			keyAttribute = rbook.getMetaData().getKey();
			insRPC = rbook.getInsertFunc();
			updRPC = rbook.getUpdateFunc();
			delRPC = rbook.getDeleteFunc();
			sqlText = ((ViewImpl)rbview).getSQL();
		} else {
			sqlText = XMLUtils.getText(XMLUtils.findChild(el, "sql"));
			// attributes
			XMLUtils.enumChildren(XMLUtils.findChild(el, "attributes"),
					new NotifyElement() {
						@Override
						public boolean notify(Element e) {
							try {
								attrs.add(new AttributeImpl(e,
										DataSourceImpl.this));
							} catch (SAXException e1) {
								RuntimeException re = new RuntimeException(e1
										.getMessage());
								re.initCause(e1);
								throw re;
							}
							return false;
						}
					});
			// set key
			keyAttribute = findAttribute(el.getAttribute("key"));
			if (keyAttribute == null)
				throw new SAXException("For key attribute not found");
			((AttributeImpl) keyAttribute).setKey(true);

			Element efunc;

			efunc = XMLUtils.findChild(el, "insert");
			if (efunc != null)
				insRPC = new RemoteFunctionImpl(efunc);

			efunc = XMLUtils.findChild(el, "update");
			if (efunc != null)
				updRPC = new RemoteFunctionImpl(efunc);

			efunc = XMLUtils.findChild(el, "delete");
			if (efunc != null)
				delRPC = new RemoteFunctionImpl(efunc);
		}
		Element edb = XMLUtils.findChild(el,"database");
		if(edb!=null) {
			dbinf = new DatabaseInfo(edb);
		}
	}

	private Attribute findAttribute(String key) {
		for (Attribute attr : attrs) {
			if (attr.getName().equalsIgnoreCase(key)) {
				return attr;
			}
		}
		return null;
	}

	private void notSupportedOnServer() {
		throw new RuntimeException("Not supported on server-side");
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public List<Attribute> getAttributes(){
		return attrs;
	}

	@Override
	public Attribute getKey() {
		return keyAttribute;
	}

	@Override
	public CacheControl getCacheControl() {
		return cc;
	}

	@Override
	public boolean isLazy() {
		return lazy;
	}

	@Override
	public boolean isReadOnly() {
		return insRPC == null && updRPC == null && delRPC == null;
	}

	@Override
	public boolean isCanAdd() {
		return insRPC != null;
	}

	@Override
	public boolean isCanDelete() {
		return delRPC != null;
	}

	@Override
	public boolean isCanEdit() {
		return updRPC != null;
	}

	@Override
	public List<DataPage> open(Map<String, Object> params) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public int getMaxSizeDataPage() {
		return sizeDataPage ;
	}

	@Override
	public Date getActualDate() {
		return (cc != CacheControl.IsModified) ? null : actualCacheDate;
	}

	@Override
	public Record findRecord(Object value, boolean onServer) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record findRecord(Object value) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public List<Record> findRecord(Map<Attribute, Object> values,
			boolean onServer) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public List<Record> findRecord(Map<Attribute, Object> values)
			throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record edit(Record r) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record add() throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record add(Record r) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public Record delete(Record r) throws Exception {
		notSupportedOnServer();
		return null;
	}

	@Override
	public void delete(List<Record> records, OnValidate onvalidate)
			throws Exception {
		notSupportedOnServer();
	}

	@Override
	public void delete(List<Record> records) throws Exception {
		notSupportedOnServer();
	}

	@Override
	public void addValidator(OnValidate onvalidate) {
		notSupportedOnServer();
	}

	@Override
	public void removeValidator(OnValidate onvalidate) {
		notSupportedOnServer();
	}
	
	public List<DataPage> innerOpen(Connection con, Map<String, Object> params, BigInteger cursorId, SessionContextImpl session)
			throws Exception {
		
		if(dbinf!=null) con = dbinf.connect();
		
		try{
			// prepare
			if (statement == null) {
				parse(sqlText, params);
				statement = con.prepareStatement(inSQL);
				statementCount = con.prepareStatement("select count(*) from("+inSQL+") as A");
			}
			// params
			int i = 1;
			for (String pname : inParams) {
				statement.setObject(i++, params.get(pname));
				statementCount.setObject(i++, params.get(pname));
			}
			// pages count
			int pcount = 0;
			try(ResultSet rs = statementCount.executeQuery()) {
				rs.next();
				int crecords = rs.getInt(1);
				pcount = crecords/sizeDataPage+crecords%sizeDataPage!=0?1:0;
			}
			if(pcount==0) return null;
			
			List<DataPage> pages = new ArrayList<DataPage>(pcount);
			// fetch data
			ResultSet rs = statement.executeQuery();
			try {
				for (int pIdx = 0; pIdx < pcount; pIdx++) {
					if(dbinf!=null)
						pages.add(new DataPageImpl(cursorId,session,rs,this,lazy, 
								pIdx==(pcount-1),dbinf.identCaseSensitive)); 
					else
						pages.add(new DataPageImpl(cursorId,session,rs,this,lazy, 
								pIdx==(pcount-1)));
				}
			} finally {
				if(!lazy) rs.close();
			}
			
			return pages;
			
		} finally {
			if(dbinf!=null) con.close();			
		}
	}

	private void parse(String sql, Map<String, Object> params)
			throws ExecuteException {
		inParams.clear();
		StringBuilder sb = new StringBuilder();
		StringTokenizer parser = new StringTokenizer(sql);
		while (parser.hasMoreTokens()) {
			String token = parser.nextToken();
			switch (token) {
			case ":":
				String pname = parser.nextToken();
				if (params.get(pname) == null)
					throw new ExecuteException("SQLParamNotFound", id + "."
							+ pname);
				inParams.add(pname);
				sb.append("?");
				break;
			default:
				sb.append(token);
			}
			sb.append(" ");
		}
		inSQL = sb.toString();
	}
	
	class DatabaseInfo {
		private String dbDriverName;
		private String dbUrl;
		private String dbSysUser;
		private String dbSysPswd;
		private Properties dbParams = new Properties();
		private Boolean identCaseSensitive;

		DatabaseInfo(Element dbel) {
			dbDriverName = dbel.getAttribute("driver");
			dbUrl = dbel.getAttribute("url");
			identCaseSensitive = new Boolean(dbel.getAttribute("ident-case-sensitive"));
			dbSysUser = dbel.getAttribute("user");
			if(dbSysUser.isEmpty()) dbSysUser = null;
			dbSysPswd = dbel.getAttribute("password");
			if(dbSysPswd.isEmpty()) dbSysPswd = null;
			XMLUtils.enumChildren(XMLUtils.findChild(dbel,"params"),new NotifyElement() {

				@Override
				public boolean notify(Element e) {
					if(e.getNodeName().equals("param")) {
						dbParams.put(e.getAttribute("name"), e.getAttribute("value"));
					}
					return false;
				}
			});
		}
		
		Connection connect() throws SQLException {
			Properties props = new Properties(dbParams);
			if(dbSysUser!=null) props.put("user", dbSysUser);
			if(dbSysPswd!=null) props.put("password", dbSysPswd);
			return DriverManager.getConnection(dbUrl,dbParams);
		}
		
	}

}
