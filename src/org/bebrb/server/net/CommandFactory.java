package org.bebrb.server.net;


import java.io.IOException;
import java.util.List;

import org.bebrb.server.net.Command.Type;
import org.bebrb.server.utils.DoubleOrInt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class CommandFactory {
	
	public CommandFactory() {
	}
	
	public static Command parse(String jsonstr) throws Exception {
		Gson gson = createGson();
		JsonArray array = gson.fromJson(jsonstr, JsonArray.class);
		//JsonParser parser = new JsonParser();
		//JsonArray array = parser.parse(jsonstr).getAsJsonArray();
		if(array.size()<2) 
			throw new Exception("Unknown format command -"+jsonstr);
		String cmdType = gson.fromJson(array.get(0), String.class);
		Command.Type typ = Type.valueOf(cmdType);
		Command cmd = (Command) gson.fromJson(array.get(1), Command.getClass(typ));
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
		return gson.toJson(new ErrorInfo(ex.getClass().getName()+":"+ex.getMessage(), 
				ex.getMessage(),smessages));
	}

	public static class NumberAdapter extends TypeAdapter<DoubleOrInt> {

		@Override
		public void write(JsonWriter out, DoubleOrInt value) throws IOException {
			out.value(value); 
		}

		@Override
		public DoubleOrInt read(JsonReader in) throws IOException {
            String json = in.nextString();
            Number n;
            if (json.indexOf('.') < 0) {
                n = Integer.valueOf(json);
            } else {
                n = Double.valueOf(json);
            }
            return new DoubleOrInt(n); 		}

		
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
		public String messageForUser;
		public String[] trace;
		
		public ErrorInfo(String message, String messageForUser,String[] trace) {
			this.message = message;
			this.messageForUser = messageForUser;
			this.trace = trace;
		}
	}
	
}
