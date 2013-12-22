package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bebrb.server.data.DataSourceImpl;
import org.bebrb.server.net.Command;
import org.bebrb.server.net.Command.Type;
import org.bebrb.server.net.CommandCallInsert;
import org.bebrb.server.net.CommandFactory;
import org.bebrb.server.net.CommandFindRecord;
import org.bebrb.server.net.CommandGetAppContext;
import org.bebrb.server.net.CommandGetRecord;
import org.bebrb.server.net.CommandGetRecords;
import org.bebrb.server.net.CommandHello;
import org.bebrb.server.net.CommandLogin;
import org.bebrb.server.net.CommandLogout;
import org.bebrb.server.net.CommandOpenDatasource;
import org.bebrb.server.net.CommandOpenReferenceView;
import org.bebrb.server.net.WriteStreamException;

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
			
			System.setProperty("bebrb.agent.name", "Beaver");
			System.setProperty("bebrb.agent.version", "1.11");
			
			// пускаем сервер
			Executors.newSingleThreadExecutor().execute(new ServerRunnable());
			Thread.sleep(1000);
			
			Command cmd;
			String sr;
			// отправляем команду Hello
			sr = send(new CommandHello());
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			CommandHello.Response response1 =  CommandFactory.createGson().fromJson(sr, CommandHello.Response.class);
			Thread.sleep(1000);
			
			// отправляем команду Login
			sr = send(new CommandLogin(response1.getApps().get(0).getName(),"sysAdmin","qwerty"));
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			CommandLogin.Response response2 = CommandFactory.createGson().fromJson(sr, CommandLogin.Response.class);
			Thread.sleep(1000);
			
			// отправляем команду GetAppContext
			sr = send(new CommandGetAppContext(response2.getSession().getId()));
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			CommandGetAppContext.Response response3 = CommandFactory.createGson().fromJson(sr, CommandGetAppContext.Response.class);
			Thread.sleep(1000);

			// отправляем команду OpenDataset
			CommandOpenDatasource cmd3 = new CommandOpenDatasource(response2.getSession().getId(),"q2", null);
			cmd3.setPageSize(10);
			cmd3.setSorting(new DataSourceImpl.SortAttribute("trade_date",true));
			sr = send(cmd3);
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			CommandOpenDatasource.Response response4 = CommandFactory.createGson().fromJson(sr, CommandOpenDatasource.Response.class);
			Thread.sleep(1000);
			
			//  отправляем команду GetRecords
			sr = send(new CommandGetRecords(response2.getSession().getId(),response4.getCursorId(),0,1));
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			Thread.sleep(1000);

			// отправляем команду OpenReferenceView
			sr = send(new CommandOpenReferenceView(response2.getSession().getId(),"r2"));
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			Thread.sleep(1000);

			// отправляем команду GetRecord
			CommandGetRecord cmd2 = new CommandGetRecord(response2.getSession().getId(),2);
			//cmd2.setDatasourceId("q1");
			cmd2.setRefId("r1");
			sr = send(cmd2);
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			Thread.sleep(1000);
			
			// отправляем команду FindRecord
			sr = send(new CommandFindRecord(response2.getSession().getId(),response4.getCursorId(),0,"3"));
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			Thread.sleep(1000);
			
			//CommandCallInsert
			CommandCallInsert cmd7 = new CommandCallInsert(response2.getSession().getId(),new ArrayList<>());
			cmd7.setReferenceBookId("r1");
			cmd7.getArgs().add(1);
			cmd7.getArgs().add(2);
			cmd7.getArgs().add(3.5);
			sr = send(cmd7);
			if(Command.getStatus(sr)!=Command.OK) throw new Exception("Error status");
			Thread.sleep(1000);
			
			
			
			// отправляем команду Logout
			send(new CommandLogout(response2.getSession().getId()));
			Thread.sleep(1000);
		 			
			System.out.println("main finish");
			
			Runtime.getRuntime().halt(0);
		} catch (Exception e) {
			e.printStackTrace();
			Runtime.getRuntime().halt(0);
		}
	}

	private static String send(Command cmd) throws IOException,
			UnknownHostException {
		Socket sock = new Socket();
		sock.connect(new InetSocketAddress(Inet4Address.getByName("localhost"),8080));
		PrintWriter out=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
		out.write(CommandFactory.toJson(cmd));
		out.flush();
		sock.shutdownOutput();
 
		BufferedReader in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
		
		String data = "";
		String s;
		while ((s = in.readLine()) != null) {
				System.out.println("response:"+s);
				data+=s;
		}
		return data;
	}

}
