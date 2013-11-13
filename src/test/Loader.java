package test;

import org.bebrb.server.ApplicationContext;

public class Loader {

	public static void main(String[] args) {
		try {
			ApplicationContext app = new ApplicationContext("Test", new ApplicationContext.Version(1,2,14));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
