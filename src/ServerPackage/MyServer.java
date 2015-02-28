package ServerPackage;

import java.io.IOException;

import java.net.ServerSocket;
/**
 * @author: Simiao Sun 
 */

public class MyServer {
	public static final int MAXCLIENTS = 3;
	private ServerSocket server_socket;

	public MyServer() {
		try {
			server_socket = new ServerSocket(10884);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ServerSocket getSocket() {
		return server_socket;
	}

	public static void main(String[] args) throws Exception {
		MyServer myserver = new MyServer();
		System.out.println("My chat room server. Version Two.\n");
		while (true) {
			/**provide service to a new login user*/
			MyService myService = new MyService(myserver.getSocket().accept());	
			myService.start();
			
		}
	}
}
