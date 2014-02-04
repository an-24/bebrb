package org.bebrb.server.data;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.bebrb.data.RemoteFunction;
import org.bebrb.reference.ReferenceBook;
import org.bebrb.reference.ReferenceBookMetaData;
import org.bebrb.reference.View;
import org.bebrb.server.DatabaseInfo;
import org.bebrb.server.ReferenceBookMetaDataImpl;
import org.bebrb.server.SessionContextImpl;
import org.bebrb.server.data.DataSourceImpl.SortAttribute;
import org.bebrb.server.utils.XMLUtils;
import org.bebrb.server.utils.XMLUtils.NotifyElement;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ReferenceBookImpl implements ReferenceBook {
	private ReferenceBookMetaDataImpl meta;
	private Map<String, View> views = new HashMap<String, View>();
	private View defaultView;
	private RemoteFunctionImpl insRPC = null;
	private RemoteFunctionImpl updRPC = null;
	private RemoteFunctionImpl delRPC = null;
	private DatabaseInfo dbinf = null;
	private String getSql;
	
	public ReferenceBookImpl(Element el) throws SAXException, IOException, ParserConfigurationException {
		meta = new ReferenceBookMetaDataImpl(el);
		XMLUtils.enumChildren(XMLUtils.findChild(el, "views"), new NotifyElement() {
			@Override
			public boolean notify(Element e) {
				try {
					View view = new ViewImpl(e,ReferenceBookImpl.this);
					views.put(view.getName(), view);
				} catch (SAXException e1) {
					RuntimeException re = new RuntimeException(e1.getMessage());
					re.initCause(e1);
					throw re;
				}
				return false;
			}
		});
		if(el.hasAttribute("default"))
			defaultView = views.get(el.getAttribute("default")); else
			defaultView = views.values().iterator().next();
		
		Element efunc;

		efunc = XMLUtils.findChild(el, "get");
		getSql = efunc.getTextContent();
		
		efunc = XMLUtils.findChild(el, "insert");
		if(efunc!=null) insRPC = new RemoteFunctionImpl(efunc);

		efunc = XMLUtils.findChild(el, "update");
		if(efunc!=null) updRPC = new RemoteFunctionImpl(efunc);
		
		efunc = XMLUtils.findChild(el, "delete");
		if(efunc!=null) delRPC = new RemoteFunctionImpl(efunc);
		
		Element edb = XMLUtils.findChild(el,"database");
		if(edb!=null) {
			dbinf = new DatabaseInfo(edb);
		}
	}

	@Override
	public ReferenceBookMetaData getMetaData() {
		return meta;
	}

	@Override
	public Map<String, View> getViews() {
		return views;
	}

	@Override
	public View getDefaultView() {
		return defaultView;
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

	public DatabaseInfo getDatabaseInfo() {
		return dbinf;
	}

	public String getGetRecordSQL() {
		return getSql;
	}

	public Connection getConnection(SessionContextImpl session) throws Exception {
		return dbinf!=null?dbinf.connect():session.getConnection();
	}
	
	public String makeHierarchySQL(ViewImpl view, String masterTable) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select A.*, (select count(*) from ").
		   append(masterTable).append(" MT ").
		   append("where MT.").append(meta.getParentKey().getName()).
		   append("=").append("A.").append(meta.getKey().getName()).
		   append(") ").append(ReferenceBookMetaData.HIERARCHY_CHILD_COUNT).append(" ").
		   append("from (").append(view.getSQL()).append(") as A ").
		   append("order by A.").append(meta.getAttrFolder().getName()).
		   append(" ").append(meta.getOrderFolder());
		DataSourceImpl ds = (DataSourceImpl) view.getDataSource();
		List<SortAttribute> sortedlist = ds.getSortedAttributes();
		if(sortedlist!=null) {
			sb.append(",").append(ds.orderBy("A", sortedlist));
		}
		return sb.toString();
	}

	@Override
	public boolean isCanAdd() {
		return insRPC!=null;
	}

	@Override
	public boolean isCanDelete() {
		return delRPC!=null;
	}

	@Override
	public boolean isCanEdit() {
		return updRPC!=null;
	}

}
