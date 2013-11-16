package org.bebrb.server.data;

import org.bebrb.data.Argument;
import org.bebrb.data.Attribute.Type;
import org.bebrb.data.RemoteFunction;
import org.w3c.dom.Element;

public class ArgumentImpl implements Argument {
	private String name;
	private Type type;
	private RemoteFunction func;

	public ArgumentImpl(Element e, RemoteFunction func) {
		this.func = func;
		name = e.hasAttribute("name")?e.getAttribute("name"):null;
		type = Type.valueOf(e.getAttribute("type"));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Type getType() {
		return type;
	}

}
