package test;

import org.bebrb.server.ApplicationContext;

public class Loader {

	public static void main(String[] args) {
		try {
			//ApplicationContext app = new ApplicationContext("Test", new ApplicationContext.Version(1,2,14));
			ApplicationContext app = new ApplicationContext("Test/1-1-1");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
