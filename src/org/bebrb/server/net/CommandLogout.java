package org.bebrb.server.net;

import java.io.OutputStream;
import java.util.logging.Level;

import org.bebrb.server.ApplicationContextImpl;
import org.bebrb.server.SessionContextImpl;

import com.google.gson.Gson;

public class CommandLogout extends Command {
	private String sessionId;

	protected CommandLogout() {
		super(Type.Logout);
	}

	public CommandLogout(String sessionId) {
		this();
		this.sessionId = sessionId;
	}
	

	@Override
	public void solution(OutputStream out) throws WriteStreamException,Exception {
		Response response = new Response();
		
		try {
			SessionContextImpl.loadSession(sessionId).logout();
		} catch (Exception e) {
			ApplicationContextImpl.getLogger().log(Level.SEVERE,e.getMessage(),e);
			// silence
		}
		Gson gson = CommandFactory.createGson();
		writeToOutputStream(out, gson.toJson(response));
	}
	
	public class Response extends Command.Response {
	}

}
