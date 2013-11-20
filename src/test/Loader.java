package test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Loader {
	
	//пример сервера
	static class ServerRunnable implements Runnable {

		@Override
		public void run() {
			ServerSocket server;
			try {
				server = new ServerSocket();
				server.bind(new InetSocketAddress(8080));
				while(true) {
					System.out.println("listiner...");
					Socket sock = server.accept();
					System.out.println("accepted");
					try {
						InputStream input = sock.getInputStream();
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						byte b = 0;
						while(b>=0) {
							b = (byte) input.read();
							if(b>=0) stream.write(b);
						}
						System.out.println(stream.toString());
						Thread.yield();
					} finally {
						sock.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {
		try {
			//ApplicationContext app = new ApplicationContext("Test", new ApplicationContext.Version(1,2,14));
			//ApplicationContext app = new ApplicationContext("Test");
			
			// пускаем сервер
			Executors.newSingleThreadExecutor().execute(new ServerRunnable());
			Thread.sleep(1000);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
