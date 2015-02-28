package ServerPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import Util.MessageType;
import Util.MyMessage;

/**
 * @author: Simiao Sun Service thread to maintain connection between the server
 *          and the client
 */
public class MyService extends Thread {
	private Socket client;// hold the socket for the current client
	private HashMap<String, String> verify = new HashMap<String, String>();
	private ArrayList<String> accounts = new ArrayList<String>();
	private String user;
	private boolean connection = false;

	public MyService(Socket client) {
		this.client = client;
		/** put in all the information about the account and the password */
		accounts.add("tom");
		accounts.add("david");
		accounts.add("beth");
		accounts.add("john");
		verify.put("tom", "tom11");
		verify.put("david", "david22");
		verify.put("beth", "beth33");
		verify.put("john", "john44");
	}

	public void run() {
		try {
			boolean logout_flag = false;
			ObjectInputStream ois = null;
			/** Check message sent from the client and prepare the response */
			while (logout_flag == false) {
				ois = new ObjectInputStream(client.getInputStream());
				MyMessage message = (MyMessage) ois.readObject();
				/** Classify received message to create different response */
				logout_flag = messageClassifier(message);
			}
			ManageService.delete_Specific_Service(user);
		} catch (IOException e) {
			System.err.println("Server IO error!");
		} catch (ClassNotFoundException e) {
			System.err.println("Server cannot find class error!");
		}

	}

	/**
	 * Classify the message according to the message type
	 * 
	 * @param message
	 * @return false only when the message type is logout
	 */
	public boolean messageClassifier(MyMessage message) {
		MessageType type = message.getType();
		if (type.getM_type().equals(MessageType.LOG_IN)) {
			toLogin(message);
		} else if (type.getM_type().equals(MessageType.ASK_LIST)) {
			toList(message);
		} else if (type.getM_type().equals(MessageType.MESSAGE)) {
			toSend(message);
		} else if (type.getM_type().equals(MessageType.LOG_OUT)) {
			toLogout(message);
			return true;
		}
		return false;
	}

	/**
	 * Respond to client's 'logout' command, inform all current online users
	 * that the message holder has left
	 * 
	 * @param message
	 */
	public void toLogout(MyMessage message) {
		String account = message.getFrom();
		if (!connection) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						client.getOutputStream());
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
				System.err.println("Logout message to message holder failure");
			}
			return;
		}

		System.out.println(account + " logout.");
		/** Inform all people that the client left the chat room */
		if (ManageService.getOnline().size() > 0) {
			for (int i = 0; i < ManageService.getOnline().size(); i++) {
				MyService mservice = ManageService
						.get_Specifc_Service(ManageService.getOnline().get(i));
				/** Try to send to every single current online users */
				try {
					ObjectOutputStream oos = new ObjectOutputStream(mservice
							.getSocket().getOutputStream());
					oos.writeObject(message);
					oos.flush();
				} catch (IOException e) {
					System.err.println("Logout message to "
							+ ManageService.getOnline().get(i) + " failure");
				}
			}
		}
	}

	/**
	 * Respond to client's 'send' command, either send to all users or send to a
	 * specific user
	 * 
	 * @param message
	 */
	public void toSend(MyMessage message) {
		if (message.isTo_all() == true) {
			/** send to all the current clients online */
			sendAll(message);
			System.out.println(message.getFrom() + ": " + message.getContent());
		} else {
			/** send to a particular client */
			sendOne(message);
		}
	}

	/**
	 * Send message to a single user that pointed by the message holder
	 * 
	 * @param message
	 */
	public void sendOne(MyMessage message) {
		String account = message.getFrom();
		String content = message.getContent();
		String account_to = message.getTo();
		if (ManageService.getOnline().contains(account_to)) {
			System.out.println(account + "(to " + account_to + "): " + content);
			MyService mservice = ManageService.get_Specifc_Service(account_to);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(mservice
						.getSocket().getOutputStream());
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
				System.err
						.println("Send message to " + account_to + " failure");
			}
		} else {
			System.out.println(account_to + " cannot reach");
		}
	}

	/**
	 * Send to all the users currently online
	 * 
	 * @param message
	 */
	public void sendAll(MyMessage message) {
		String account = message.getFrom();
		for (int i = 0; i < ManageService.getOnline().size(); i++) {
			if (!ManageService.getOnline().get(i).equals(account)) {
				MyService mservice = ManageService
						.get_Specifc_Service(ManageService.getOnline().get(i));
				/** Try to send message to every single user */
				try {
					ObjectOutputStream oos = new ObjectOutputStream(mservice
							.getSocket().getOutputStream());
					oos.writeObject(message);
					oos.flush();
				} catch (IOException e) {
					System.err.println("Send message to "
							+ ManageService.getOnline().get(i) + " failure");
				}

			}
		}
	}

	/**
	 * Respond to client's 'who' command, send all the current online users name
	 * to the client
	 * 
	 * @param message
	 */
	public void toList(MyMessage message) {
		message.setOnline(ManageService.getOnline());
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					client.getOutputStream());
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			System.err.println("User list back to message holder failure");
		}

	}

	/**
	 * Respond to client's 'login' command, send back the result of the
	 * verification
	 * 
	 * @param message
	 */
	public void toLogin(MyMessage message) {
		String account = message.getFrom();
		String password = message.getPassword();
		this.connection = accounts.contains(account) && !ManageService.getOnline().contains(account) && verify.get(account).equals(password) && ManageService.getOnline().size() < MyServer.MAXCLIENTS;
		sendLoginResult(message, this.connection);

	}

	/**
	 * Send back the result of the verification When the login_flag is true:
	 * Inform every the client's login When the login_flag is false: Inform only
	 * the client the verification failure
	 * 
	 * @param message
	 * @param login_flag
	 */
	public void sendLoginResult(MyMessage message, boolean login_flag) {
		message.setLoginResult(login_flag);
		
		if (!login_flag) {
			/** Inform the client the verification failure */
			this.sendMessage(message, client);
			return;
		}
		
		String account = message.getFrom();
		ManageService.addService(this, account);
		ManageService.addOnline(account);
		this.setUser(account);
		System.out.println(account + " login");
		for (int i = 0; i < ManageService.getOnline().size(); i++) {
			MyService mservice = ManageService
					.get_Specifc_Service(ManageService.getOnline().get(i));
			/** Inform every single user that is currently online about the login */
			this.sendMessage(message, mservice.getSocket());

		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Socket getSocket() {
		return client;
	}

	public boolean isConnection() {
		return connection;
	}

	public void setConnection(boolean connection) {
		this.connection = connection;
	}

	private boolean sendMessage(MyMessage message, Socket receiverSocket) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					receiverSocket.getOutputStream());
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			System.err.println("Error! Message sending error!");
			return false;
		}
		return true;
	}
}
