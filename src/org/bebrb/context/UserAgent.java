package org.bebrb.context;

import java.util.Locale;

public class UserAgent {
	public final String agent = System.getProperty("bebrb.agent.name");
	public final String agentVersion = System.getProperty("bebrb.agent.version");
	public final String agentOS = System.getProperty("os.name");
	public final String agentOSArch = System.getProperty("os.arch");
	public final String agentOSVersion = System.getProperty("os.version");
	public final String lang = Locale.getDefault().getLanguage();
}
