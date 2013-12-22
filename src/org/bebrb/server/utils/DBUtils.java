package org.bebrb.server.utils;

import java.io.InputStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;

import org.bebrb.data.Attribute.Type;
import org.bebrb.server.net.ExecuteException;
import org.bebrb.utils.Base64;

public class DBUtils {
	
	public static int indexOfFieldByName(ResultSetMetaData rsmeta, String fname) throws SQLException {
		for (int i = 1, len = rsmeta.getColumnCount(); i <= len; i++) {
			String l = rsmeta.getColumnLabel(i);
			if(l.equalsIgnoreCase(fname)) return i;
		};
		return -1;
	}

	public static Object cast(String v, org.bebrb.data.Attribute.Type type) throws ExecuteException {
		try {
			switch (type) {
			case Money:		
			case Integer:	return new Integer(v);
			
			case String:	return v;
			
			case Double:	return new Double(v);
			
			case Date:		return DateFormat.getInstance().parse(v);
			
			case Boolean:	return new Boolean(v);
			
			case Image:		
			case Text:
			case Blob:
				break;
			}
			throw new ExecuteException("UnknowTypeCastError",type.name());
		} catch (Exception e) {
			ExecuteException eex = new ExecuteException("TypeCastError",type.name(),v);
			eex.initCause(e);
			throw eex;
		}
	}

	public static Object castFromObject(Object arg, Type type) throws ExecuteException {
		try {
			switch (type) {
			case Money:		
			case Integer:	{
				if(arg instanceof Integer) return arg;
				
				if (arg instanceof String) return new Integer((String)arg);else
					if (arg instanceof Double) return ((Double)arg).intValue();else
						return new Integer(arg.toString());
			}
			
			case String:	return arg.toString();

			case Double:	{
				if(arg instanceof Double) return arg;
				
				if (arg instanceof String) return new Double((String)arg);else
					if (arg instanceof Integer) return new Double((Integer)arg);else
						return new Double(arg.toString());
			}
			case Date:		return DateFormat.getInstance().parse((String)arg);
			case Boolean:	{
				if(arg instanceof Boolean) return arg;
				
				if (arg instanceof String) return new Boolean((String)arg);else
					if (arg instanceof Integer) return new Boolean((Integer)arg!=0);else
						return new Boolean(arg.toString());
				
			}
			
			case Text:
				return (String)arg;
				
			case Image:		
			case Blob: {
				if(arg instanceof byte[]) return arg;
				if (arg instanceof String) return Base64.decode((String) arg); else
					if (arg instanceof InputStream) {
						InputStream in = (InputStream)arg;
						byte[] buf = new byte[in.available()];
						in.read(buf);
						return buf; 
					} else	return Base64.decode(arg.toString());
			}
			}
			throw new ExecuteException("UnknowTypeCastError",type.name());
		} catch (Exception e) {
			ExecuteException eex = new ExecuteException("TypeCastError",type.name(),arg);
			eex.initCause(e);
			throw eex;
		}
	}


}
