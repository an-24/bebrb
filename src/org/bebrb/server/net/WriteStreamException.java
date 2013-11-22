package org.bebrb.server.net;

@SuppressWarnings("serial")
public class WriteStreamException extends Exception {

	public WriteStreamException(Throwable e) {
		super(e.getMessage());
		initCause(e);
	}

}
