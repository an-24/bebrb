package org.bebrb.server.net;

import org.bebrb.server.ApplicationContextImpl;

@SuppressWarnings("serial")
public class ExecuteException extends Exception {

	public ExecuteException(String id) {
		super(ApplicationContextImpl.getStrings().getString(id));
	}

}
