package org.bebrb.server;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bebrb.context.Config;
import org.bebrb.context.SessionContext;
import org.bebrb.context.UserContext;

public class SessionContextImpl implements SessionContext {
	
	private BigInteger id;
	private UserContext user;
	private ApplicationContext appContext;
	private Logger log;
	
	private static Map<BigInteger,SessionContextImpl> sessions = new HashMap<BigInteger,SessionContextImpl>(); 

	protected SessionContextImpl(ApplicationContext appContext, BigInteger id, UserContext user)	{
		this.appContext = appContext;
		this.id = id;
		this.user = user;
		this.log = Logger.getLogger("bebrb.session."+user.getUser().getLoginName());
	}

	protected SessionContextImpl(ApplicationContext appContext, UserContext user)	{
		this(appContext,newId(),user);
		sessions.put(id, this);
	}
	
	public static SessionContext newSession(ApplicationContext appContext, UserContext user) {
		return new SessionContextImpl(appContext,user);
	}
	
	public static SessionContext loadSession(String id) {
		return sessions.get(new BigInteger(id,16));
	}
	
	private static BigInteger newId() {
		byte[] code = new byte[16];
		new SecureRandom().nextBytes(code);
		return new BigInteger(1,code);
	}

	@Override
	public Config getConfig() {
		return null;
	}

	@Override
	public String getId() {
		return id.toString(16);
	}

	@Override
	public UserContext getActiveUserContext() {
		return user;
	}

	@Override
	public void logout() {
		sessions.remove(id);
		log.info("Logout session id "+id);
		user = null;
		id = null;
	}

	@Override
	public Logger getLogger() {
		return log;
	}

}
