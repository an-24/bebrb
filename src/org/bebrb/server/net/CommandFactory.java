package org.bebrb.server.net;


import org.bebrb.server.net.Command.Type;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class CommandFactory {
	
	public CommandFactory() {
	}
	
	public static Command parse(String jsonstr) throws Exception {
		Gson gson = new Gson();
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
		Gson gson = new Gson();
		scmd+=gson.toJson(cmd)+"]";
		return scmd;
	}
	
	public static Command newCommand(Type type) throws Exception {
		return (Command) Command.getClass(type).newInstance();
	}
	
}
