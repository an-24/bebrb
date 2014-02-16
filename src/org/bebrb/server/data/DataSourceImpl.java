package org.bebrb.server.data;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import org.bebrb.reference.ReferenceBookMetaData.ReferenceType;
import org.bebrb.server.DataSources;
import org.bebrb.server.DatabaseInfo;
import org.bebrb.server.ReferenceBookMetaDataImpl;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.net.ExecuteException;
import org.bebrb.server.utils.DBUtils;
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
	// parse sql & params
	private String inSQL = null;
	private List<String> inParams = new ArrayList<>();
	private DatabaseInfo dbinf = null;
	private ViewImpl view;
	private boolean pub;
	private String name;
	
	private PreparedStatement statementCount;
	// mutex for statementCount
	private Object csStatementCount = new Object();

	public DataSourceImpl(ReferenceBook ref, View view, Date actualDate) {
		id = ref.getMetaData().getId()+"."+view.getName();
		name = ref.getMetaData().getName()+"."+view.getName();
		lazy = view.isLazy();
		pub = true; // data source for view have type "public" always
		cc = ref.getMetaData().getCacheControl();
		if (cc == DataSource.CacheControl.IsModified)
			if (actualDate==null)
				actualCacheDate = new Date();
			else
				actualCacheDate = actualDate;
		sqlText = ((ViewImpl)view).getSQL();
		attrs.addAll(ref.getMetaData().getAttributes());
		keyAttribute = ref.getMetaData().getKey();
		insRPC = ((ReferenceBookImpl)ref).getInsertFunc();
		updRPC = ((ReferenceBookImpl)ref).getUpdateFunc();
		delRPC = ((ReferenceBookImpl)ref).getDeleteFunc();
		dbinf = ((ReferenceBookImpl)ref).getDatabaseInfo();
		
	}
			
	public DataSourceImpl(Element el, DataSources dscontext) throws Exception {
		id = el.getAttribute("id");
		name = el.getAttribute("name");
		lazy = Boolean.parseBoolean(el.getAttribute("lazy"));
		pub = Boolean.parseBoolean(el.getAttribute("public"));
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
			name = rbook.getMetaData().getName();
			keyAttribute = rbook.getMetaData().getKey();
			insRPC = ((ReferenceBookImpl)rbook).getInsertFunc();
			updRPC = ((ReferenceBookImpl)rbook).getUpdateFunc();
			delRPC = ((ReferenceBookImpl)rbook).getDeleteFunc();
			sqlText = ((ViewImpl)rbview).getSQL();
		} else {
			sqlText = XMLUtils.getTextContent(XMLUtils.findChild(el, "sql"));
			// attributes
			XMLUtils.enumChildren(XMLUtils.findChild(el, "attributes"),
					new NotifyElement() {
						@Override
						public boolean notify(Element e) {
							try {
								attrs.add(new AttributeImpl(DataSourceImpl.this,e));
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
	public String getName() {
		return name;
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
	
	public OpenedDataSet innerOpen(Connection con, Map<String, Object> params,  
			SessionContextImpl session, final List<SortAttribute> sortedAttributes, int pageSize)
			throws Exception {
		
		if(dbinf!=null) con = dbinf.connect();
		
		if(pageSize<0) pageSize = sizeDataPage; 
		
		OpenedDataSet result = new OpenedDataSet();
		result.cursorId = newCursorId();
		try{
			String masterTable=null;
			// parse
			if(inSQL==null) {
				inSQL = parse(id,sqlText,params,inParams);
				statementCount = con.prepareStatement("select count(*) from("+inSQL+") as A");
			}
			// prepare
			PreparedStatement statement;
			// for hierarchy reference book. Request main table if reference book
			boolean hierarchyRBook = view!=null && view.getReferenceBook().getMetaData().getReferenceType()==ReferenceType.Hierarchy;
			if(hierarchyRBook) {
				masterTable = ((ReferenceBookMetaDataImpl)view.getReferenceBook().getMetaData()).getMasterTable();
				// try request db
				if(masterTable==null) {
					String keyName = view.getReferenceBook().getMetaData().getKey().getName();
					try(PreparedStatement statementTmp = con.prepareStatement("select * from("+inSQL+") as A")){
						ResultSetMetaData rsmeta = statementTmp.getMetaData();
						masterTable = rsmeta.getTableName(DBUtils.indexOfFieldByName(rsmeta, keyName));
					}; 
				}
			}
			if(hierarchyRBook) {
				statement = con.prepareStatement(((ReferenceBookImpl)view.getReferenceBook()).makeHierarchySQL(view, masterTable,sortedAttributes));
			} else
				if(sortedAttributes!=null && sortedAttributes.size()>0) 
						 statement = con.prepareStatement("select * from("+inSQL+") as A order by "+orderBy("A",sortedAttributes));
					else statement = con.prepareStatement(inSQL);
			
			// pages count
			int pcount = 0;
			// warning! potential bottleneck
			synchronized (csStatementCount) {
				// params
				int i = 1;
				for (String pname : inParams) {
					statementCount.setObject(i++, params.get(pname));
				}
				try(ResultSet rs = statementCount.executeQuery()) {
					rs.next();
					result.recordCount = rs.getInt(1);
					pcount = result.recordCount/pageSize+(result.recordCount%pageSize!=0?1:0);
				}
				if(pcount==0) return null;
			}
			
			// main query
			int i = 1;
			for (String pname : inParams) {
				statement.setObject(i++, params.get(pname));
			}
			List<DataPage> pages = new ArrayList<DataPage>(pcount);
			// fetch data
			ResultSet rs = statement.executeQuery();
			try {
				for (int pIdx = 0; pIdx < pcount; pIdx++) {
					if(dbinf!=null)
						pages.add(new DataPageImpl(result.cursorId,session,rs,this,lazy, 
								pIdx==(pcount-1),dbinf.isIdentCaseSensitive())); 
					else
						pages.add(new DataPageImpl(result.cursorId,session,rs,this,lazy, 
								pIdx==(pcount-1)));
				}
			} finally {
				if(!lazy) {
					rs.close();
					statement.close();
				}
			}
			
			result.pages = pages;
			return result;
			
		} finally {
			if(dbinf!=null) con.close();			
		}
	}
	
	public void reset(){
		try {
			if(statementCount!=null) statementCount.close();
			statementCount = null;
		} catch (SQLException e) {
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		reset();
	}

	public static String parse(String dsId, String sql, Map<String, Object> params,List<String> inParams)
			throws ExecuteException {
		inParams.clear();
		StringBuilder sb = new StringBuilder();
		StringTokenizer parser = new StringTokenizer(sql," \t\n\r\f=",true);
		while (parser.hasMoreTokens()) {
			String token = parser.nextToken();
			if(token.startsWith(":")) {
				String pname = token.substring(1);
				if(params==null || !params.containsKey(pname))
					throw new ExecuteException("SQLParamNotFound",dsId+"."+pname);
				inParams.add(pname);
				sb.append("?");
			} else sb.append(token);
		}
		return sb.toString();
	}

	private BigInteger newCursorId() {
		if(isLazy()) {
			byte[] code = new byte[16];
			new SecureRandom().nextBytes(code);
			return new BigInteger(1,code);
		};
		return null;
	}

	public Connection getConnection(SessionContextImpl session) throws Exception {
		return dbinf!=null?dbinf.connect():session.getConnection();
	}

	public String getSqlText() {
		return sqlText;
	}

	public static String orderBy(String alias,
			List<SortAttribute> sortedAttributes) {
		StringBuffer order = new StringBuffer();
		for (int i = 0; i < sortedAttributes.size(); i++) {
			SortAttribute attr = sortedAttributes.get(i);
			order.append(alias+".");
			order.append(attr.name);
			if(attr.desc)
				order.append(" desc");
			if(i<sortedAttributes.size()-1) order.append(",");
		}
		return order.toString();
	}
	
	static public class SortAttribute {
		public final String name;
		public final boolean desc;
		
		public SortAttribute(String name, boolean desc) {
			this.name = name;
			this.desc = desc;
		}

		public SortAttribute(String name) {
			this.name = name;
			this.desc = false;
		}

	}

	public void setReferenceBookView(View view) {
		this.view =  (ViewImpl) view;
	}

	public RemoteFunction getInsertFunc() {
		return insRPC;
	}

	public RemoteFunction getUpdateFunc() {
		return updRPC;
	}

	public RemoteFunction getDeleteFunc() {
		return delRPC;
	}

	public boolean isPublished() {
		return pub;
	}

	@Override
	public void open(Map<String, Object> params, OnOpen callback) {
		notSupportedOnServer();
	}

	@Override
	public void stop() {
		notSupportedOnServer();
	}

	@Override
	public void close() {
		notSupportedOnServer();
	}

	@Override
	public boolean isOpen() {
		notSupportedOnServer();
		return false;
	}

	public class OpenedDataSet {
		BigInteger cursorId;
		List<DataPage> pages;
		int recordCount;
		
		public List<DataPage> getPages() {
			return pages;
		}
		public int getRecordCount() {
			return recordCount;
		}
		public BigInteger getCursorId() {
			return cursorId;
		}
	}
}
