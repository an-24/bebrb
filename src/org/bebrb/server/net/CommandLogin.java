package org.bebrb.server.net;

import java.io.OutputStream;

public class CommandLogin extends Command {

	protected CommandLogin() {
		super(Type.Login);
	}

	static class LoginInfo {
		public final String user;
		public final String pswd;
		public final String agentOS;
		public final String agentArch;
		public final String agentVersion;

		public LoginInfo(String user, String pswd, String agentOS,
				String agentArch, String agentVersion) {
			super();
			this.user = user;
			this.pswd = pswd;
			this.agentOS = agentOS;
			this.agentArch = agentArch;
			this.agentVersion = agentVersion;
		}
	}
	
	@Override
	public void solution(OutputStream out) throws WriteStreamException,
			Exception {
		// TODO Auto-generated method stub
	}

}
