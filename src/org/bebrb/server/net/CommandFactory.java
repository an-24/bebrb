package org.bebrb.server.net;


import org.bebrb.server.net.Command.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class CommandFactory {
	
	public CommandFactory() {
	}
	
	public static Command parse(String jsonstr) throws Exception {
		Gson gson = createGson();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(jsonstr).getAsJsonArray();
		if(array.size()<2) 
			throw new Exception("Unknown format command -"+jsonstr);
		String cmdType = gson.fromJson(array.get(0), String.class);
		Command cmd = (Command) gson.fromJson(array.get(1), Command.getClass(Type.valueOf(cmdType)));
		return cmd;
		
	}
	
	public static String toJson(Command cmd) {
		String scmd = "[\""+cmd.type.toString()+"\",";
		Gson gson = createGson();
		scmd+=gson.toJson(cmd)+"]";
		return scmd;
	}

	public static String toJson(Throwable ex) {
		Gson gson = createGson();
		StackTraceElement[] stack = ex.getStackTrace();
		final String[] smessages = new String[stack.length];
		for (int i = 0; i < stack.length; i++) 
			smessages[i] = stack[i].toString();
		return gson.toJson(new ErrorInfo(ex.getClass().getName()+":"+ex.getMessage(),smessages));
	}

	public static Gson createGson() {
		return new GsonBuilder()
				.setDateFormat("dd.MM.yyyy").create();
	}
	
	public static Command newCommand(Type type) throws Exception {
		return (Command) Command.getClass(type).newInstance();
	}

	static class ErrorInfo {
		public int status = 100;
		public String message;
		public String[] trace;
		
		public ErrorInfo(String message, String[] trace) {
			this.message = message;
			this.trace = trace;
		}
	}
	
}
