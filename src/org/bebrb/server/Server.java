package org.bebrb.server;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bebrb.server.net.Command;
import org.bebrb.server.net.CommandFactory;
import org.bebrb.server.net.WriteStreamException;

public class Server {
	
	private static int port = 8080;
	private static int queueDepth = 20;
	
	public static void main(String[] args) {
		String s;
		s = System.getProperty("org.bebrb.port");
		if(s!=null) port = new Integer(s);
		s = System.getProperty("org.bebrb.queue-depth");
		if(s!=null) queueDepth = new Integer(s);
		
		Executors.newSingleThreadExecutor().execute(new ServerRunnable());
	}
	
	static class ServerRunnable implements Runnable {

		@Override
		public void run() {
			System.out.println("server started");
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
					System.out.println("request:"+cmd);
					OutputStream out = sock.getOutputStream();
					try {
						cmd.solution(out);
						sock.shutdownOutput();
					} catch(WriteStreamException ex) {
						// net problem
						sock.shutdownOutput();
						ex.printStackTrace();
					} catch(Exception th) {
						Writer writer = new BufferedWriter(new OutputStreamWriter(out));
						writer.write(CommandFactory.toJson(th));
						writer.flush();
						sock.shutdownOutput();
					}	
						
				} finally {
					sock.close();		
				};
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
