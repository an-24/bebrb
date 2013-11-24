package org.bebrb.server.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


/**
 * Base class for net command. Net command send is a message of JSON format. 
 * <pre>
 * [&lt;CommandTypeName&gt;,{&lt;ObjectCommand&gt;}]
 * </pre>
 * CommandTypeName is {@link Command.Type}. ObjectCommand - object with the command options. For example:
 * <pre>
 * ["Hello",{}]
 * ["Login",{app:"test",user:"sys",pswd:"---"}]
 * ["Logout",{session:"ACD1234BD3459001D"}]
 * </pre>
 */
public abstract class Command {
	/**
	 * Type of net command
	 * <li>Hello - welcome message. The answer to the query as a list of applications.  
	 *
	 */
	public static enum Type {Hello,Login,Logout};
	private static Class<?>[] classes = {
		CommandHello.class,
		CommandLogin.class,
		CommandLogout.class
	};
	
	public transient final Type type;
	
	protected Command(Type t) {
		type = t;
	}

	public static Class<?> getClass(Type t) {
		return classes[t.ordinal()];
	}
	
	public abstract void solution(OutputStream out) throws WriteStreamException, Exception;
	
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
