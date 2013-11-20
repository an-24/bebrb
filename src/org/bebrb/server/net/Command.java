package org.bebrb.server.net;


/**
 * Base class for net command. Net command send is a message of JSON format. 
 * <pre>
 * [&lt;CommandTypeName&gt;,{&lt;ObjectCommand&gt;}]
 * </pre>
 * CommandTypeName is {@link Command.Type}. ObjectCommand - object with the command options. For example:
 * <pre>
 * ["Hello",{}]
 * ["Login",{user:"sys",pswd:"---"}]
 * ["Logout",{session:"ACD1234BD3459001D"}]
 * </pre>
 */
public class Command {
	/**
	 * Type of net command
	 * <li>Hello - welcome message. The answer to the query as a list of applications.  
	 *
	 */
	public static enum Type {Hello,Login,Logout};
	private static Class<?>[] classes = {
		CommandHello.class
	};
	
	public transient final Type type;
	
	protected Command(Type t) {
		type = t;
	}

	public static Class<?> getClass(Type t) {
		return classes[t.ordinal()];
	}
	
	public String toString() {
		return CommandFactory.toJson(this);
	}
	
}
