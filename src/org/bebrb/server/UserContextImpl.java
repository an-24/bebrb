package org.bebrb.server;

import org.bebrb.context.Config;
import org.bebrb.context.UserAgent;
import org.bebrb.context.UserContext;
import org.bebrb.user.User;

public class UserContextImpl implements UserContext {
	private User user;
	private UserAgent agent;

	public UserContextImpl(User user, UserAgent agent) {
		this.user = user;
		this.agent = agent;
	}

	@Override
	public Config getConfig() {
		return null;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public UserAgent getAgentInfo() {
		return agent;
	}

}
