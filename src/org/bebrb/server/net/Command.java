package org.bebrb.server.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;


/**
 * Base class for net command. Net command send is a message of JSON format. 
 * <pre>
 * [&lt;CommandTypeName&gt;,{&lt;ObjectCommand&gt;}]
 * </pre>
 * CommandTypeName is {@link Command.Type}. ObjectCommand - object with the command options. For example:
 * <pre>
 * ["Hello",{}]
 * ["Login",{app:"test",user:"sys",pswd:"---"}]
 * ["Logout",{sessionId:"ACD1234BD3459001D"}]
 * </pre>
 */
public abstract class Command {
	public static final int OK = 0;
	public static final int ERROR = 100;

	/**
	 * Type of net command
	 * <li>Hello - welcome message. The answer to the query as a list of applications.  
	 *
	 */
	public static enum Type {Hello,Login,Logout,GetAppContext,OpenDatasource,
							OpenReferenceView, GetRecord, GetRecords, FindRecord,
							Insert,Update,Delete}
	private static Class<?>[] classes = {
		CommandHello.class,
		CommandLogin.class,
		CommandLogout.class,
		CommandGetAppContext.class,
		CommandOpenDataSource.class,
		CommandOpenReferenceView.class,
		CommandGetRecord.class,
		CommandGetRecords.class,
		CommandFindRecord.class,
		CommandCallInsert.class,
		CommandCallUpdate.class,
		CommandCallDelete.class
	};
	
	public transient final Type type;

	public static class Response {
		int status;
		String message;
		String messageForUser;
		List<String> trace;
		
		public List<String> getTrace() {
			return trace;
		}
		public void setTrace(List<String> trace) {
			this.trace = trace;
		}
		public int getStatus() {
			return status;
		}
		public String getMessage() {
			return message;
		}
		public String getMessageForUser() {
			return messageForUser!=null?messageForUser:message;
		}
	}
	
	protected Command(Type t) {
		type = t;
	}

	public static Class<?> getClass(Type t) {
		return classes[t.ordinal()];
	}
	
	public abstract void solution(OutputStream out) throws WriteStreamException, Exception;
	
	public static int getStatus(String response) {
		Response result = CommandFactory.createGson().fromJson(response, Command.Response.class);
		return result==null?ERROR:result.status;
	}
	
	protected void writeToOutputStream(OutputStream out, String data) throws WriteStreamException {
		OutputStreamWriter wr = new OutputStreamWriter(out);
		Writer writer = new BufferedWriter(wr);
		try {
			writer.write(data);
			writer.flush();
		} catch (IOException e) {
			throw new WriteStreamException(e);
		}
	}

	protected void writeToOutputStream(OutputStream out, InputStream in) throws WriteStreamException {
		try {
			byte[] buff = new byte[1024];
			int n = buff.length;
			while(n==buff.length) {
				n = in.read(buff);
				out.write(buff, 0, n);
			}
		} catch (IOException e) {
			throw new WriteStreamException(e);
		}
	}
	
	public String toString() {
		return CommandFactory.toJson(this);
	}
	
}
