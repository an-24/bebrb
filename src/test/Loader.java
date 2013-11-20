package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bebrb.server.net.Command;
import org.bebrb.server.net.Command.Type;
import org.bebrb.server.net.CommandFactory;

public class Loader {
	
	static class HandlerRunnable implements Runnable {
		
		private String request;
		private Socket sock;

		public HandlerRunnable(Socket sock,String request) {
			this.request = request;
			this.sock = sock;
		}

		@Override
		public void run() {
			try {
				try {
					Command cmd = CommandFactory.parse(request);
					OutputStreamWriter out = new OutputStreamWriter(sock.getOutputStream());
					Writer writer = new BufferedWriter(out);
					System.out.println(cmd);
				} finally {
					sock.close();		
				};
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//пример сервера
	static class ServerRunnable implements Runnable {
		private int port = 8080;
		private int queueDepth = 5;

		@Override
		public void run() {
			ServerSocket server;
			try {
				ExecutorService exec = Executors.newFixedThreadPool(queueDepth);
				server = new ServerSocket();
				server.bind(new InetSocketAddress(port));
				while(true) {
					System.out.println("listiner...");
					Socket sock = server.accept();
					System.out.println("accepted");
					InputStream input = sock.getInputStream();
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					byte b = 0;
					while(b>=0) {
						b = (byte) input.read();
						if(b>=0) stream.write(b);
					}
					exec.execute(new HandlerRunnable(sock,stream.toString()));
					Thread.yield();
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
			
			// отправляем команду
			Socket sock = new Socket();
			sock.connect(new InetSocketAddress(Inet4Address.getByName("localhost"),8080));
            PrintWriter out=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			out.write(CommandFactory.toJson(CommandFactory.newCommand(Type.Hello)));
			out.flush();
			sock.shutdownOutput();
		 
            BufferedReader in=new BufferedReader(new InputStreamReader(sock.getInputStream()));    
			String data;
			System.out.println("Output from Server .... \n");
			while ((data = in.readLine()) != null) {
					System.out.println(data);
			}
		 			
			System.out.println("main finish");			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
