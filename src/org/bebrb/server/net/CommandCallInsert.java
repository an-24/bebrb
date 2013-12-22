package org.bebrb.server.net;

import java.util.List;


public class CommandCallInsert extends CommandCall {

	public CommandCallInsert() {
		super(Type.Insert);
	}
	
	public CommandCallInsert(String sessionId) {
		super(Type.Insert,sessionId);
	}

	public CommandCallInsert(String sessionId, List<Object> args) {
		this(sessionId);
		setArgs(args);
	}
	
}
