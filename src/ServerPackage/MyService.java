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
 * @author: Simiao Sun
 */
public class MyService extends Thread {
	private Socket client;// hold the socket for the current client
	private HashMap<String, String> verify = new HashMap<String, String>();
	private ArrayList<String> accounts = new ArrayList<String>();
	private String user;

	public MyService(Socket client) {
		this.client = client;
		// put in all the information about the account and the password
		accounts.add("tom");
		accounts.add("david");
		accounts.add("beth");
		accounts.add("john");
		verify.put("tom", "tom11");
		verify.put("david", "david22");
		verify.put("beth", "beth33");
		verify.put("john", "john44");
	}

	public Socket getSocket() {
		return client;
	}

	public void run() {
		try {
			boolean logout = false;
			ObjectInputStream ois = null;
			while (logout == false) {
				ois = new ObjectInputStream(client.getInputStream());
				MyMessage ms = (MyMessage) ois.readObject();
				MessageType type = ms.getType();
				if (type.getM_type().equals(MessageType.LOG_IN)) {
					toLogin(ms);

				} else if (type.getM_type().equals(MessageType.ASK_LIST)) {
					toList(ms);
				} else if (type.getM_type().equals(MessageType.MESSAGE)) {
					toSend(ms);

				} else if (type.getM_type().equals(MessageType.LOG_OUT)) {
					toLogout(ms);
					logout = true;
				}
			}
			ManageService.delete_Specific_Service(user);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void toLogout(MyMessage ms) {
		String account = ms.getFrom();
		System.out.println(account + " logout.");
		// Inform all people that the client left the chat room
		if (ManageService.getOnline().size() > 0) {
			for (int i = 0; i < ManageService.getOnline().size(); i++) {

				MyService mservice = ManageService
						.get_Specifc_Service(ManageService.getOnline().get(i));
				try {
					ObjectOutputStream oos = new ObjectOutputStream(mservice
							.getSocket().getOutputStream());
					oos.writeObject(ms);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void toSend(MyMessage message) {
		// Check is this message meant to send to all or not
		if (message.isTo_all() == true) {
			// send to all the current clients online
			sendAll(message);
			System.out.println(message.getFrom() + ": " + message.getContent());
		} else {
			// send to a particular client
			sendOne(message);
		}
	}

	private void sendOne(MyMessage message) {
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
				e.printStackTrace();
			}
		} else {
			System.out.println(account_to + " cannot reach");
		}
	}

	private void sendAll(MyMessage message) {
		String account = message.getFrom();

		for (int i = 0; i < ManageService.getOnline().size(); i++) {
			if (!ManageService.getOnline().get(i).equals(account)) {
				MyService mservice = ManageService
						.get_Specifc_Service(ManageService.getOnline().get(i));
				try {
					ObjectOutputStream oos = new ObjectOutputStream(mservice
							.getSocket().getOutputStream());
					oos.writeObject(message);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void toList(MyMessage message) {
		message.setOnline(ManageService.getOnline());
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					client.getOutputStream());
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void toLogin(MyMessage message) {
		String account = message.getFrom();
		String password = message.getPassword();
		if (accounts.contains(account)
				&& !ManageService.getOnline().contains(account)
				&& verify.get(account).equals(password)
				&& ManageService.getOnline().size() < MyServer.MAXCLIENTS) {
			sendLoginResult(message, true);
		} else {
			sendLoginResult(message, false);

		}

	}

	public void sendLoginResult(MyMessage message, boolean login_flag) {
		if (login_flag) {
			message.setLoginResult(true);
			String account = message.getFrom();
			ManageService.addService(this, account);
			ManageService.addOnline(account);
			this.setUser(account);
			System.out.println(account + " login");
			for (int i = 0; i < ManageService.getOnline().size(); i++) {
				MyService mservice = ManageService
						.get_Specifc_Service(ManageService.getOnline().get(i));
				try {
					ObjectOutputStream oos = new ObjectOutputStream(mservice
							.getSocket().getOutputStream());
					oos.writeObject(message);
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			message.setLoginResult(false);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						client.getOutputStream());
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
