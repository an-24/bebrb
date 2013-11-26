package org.bebrb.utils;

import java.util.Date;

public class Version {
	public final int major;
	public final int minor;
	public final int build;
	
	private Date release;

	public Version(int major, int minor, int build) {
		this.major = major;
		this.minor = minor;
		this.build = build;
	}
	public Version(String v) {
		String[] vers = v.split("-");
		major = new Integer(vers[0]);
		minor = new Integer(vers[1]);
		build = new Integer(vers[2]);
	}

	public Date getRelease() {
		return release;
	}
	public void setRelease(Date release) {
		this.release = release;
	}

	public String toString() {
		return major+"-"+minor+"-"+build;
	}
}
