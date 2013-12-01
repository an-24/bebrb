package org.bebrb.server.net;

import org.bebrb.server.ApplicationContextImpl;

@SuppressWarnings("serial")
public class ExecuteException extends Exception {

	public ExecuteException(String id) {
		super(ApplicationContextImpl.getStrings().getString(id));
	}

	public ExecuteException(String id,Object ... par) {
		super(String.format(ApplicationContextImpl.getStrings().getString(id),par));
	}
}
