package ServerPackage;

import java.io.IOException;

import java.net.ServerSocket;

/**
 * @author: Simiao Sun
 */

public class MyServer {
	/**set limit for the number of client*/
	public static final int MAXCLIENTS = 3;
	private ServerSocket serverSocket;
	/**detect the port validation*/
	private boolean portDetector = true;

	public MyServer() {
		try {
			serverSocket = new ServerSocket(10884);
		} catch (IOException e) {
			System.err.println("Error!Port already in use");
			this.setPortDetector(false);
		}

	}

	public ServerSocket getSocket() {
		return serverSocket;
	}

	/**
	 * @return the portDetector
	 */
	public boolean isPortDetector() {
		return portDetector;
	}

	/**
	 * @param portDetector
	 *            the portDetector to set
	 */
	public void setPortDetector(boolean portDetector) {
		this.portDetector = portDetector;
	}

	public static void main(String[] args) {
		MyServer myserver = new MyServer();
		if (myserver.isPortDetector()) {
			System.out.println("My chat room server. Version Two.\n");
			while (true) {
				/** provide service to a new login user */
				MyService myService;
				try {
					myService = new MyService(myserver.getSocket().accept());
					myService.start();
				} catch (IOException e) {
					System.err.println("Server service accpetion error");
				}
			}
		}
	}
}
