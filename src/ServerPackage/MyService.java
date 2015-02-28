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
	// hold the socket for the current client
	private Socket client;
	/** hold the account and the password according to the account*/
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

		// Accept the current client
		try {
			boolean logout = false;// check whether the client wants to logout
									// or not
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			while (logout == false) {
				// Read the message sent from the client
				ois = new ObjectInputStream(client.getInputStream());
				MyMessage ms = (MyMessage) ois.readObject();
				MessageType type = ms.getType();
				/*
				 * // Need to add a service when the client first send a message
				 * to // the server if (num == 0) {
				 * ManageService.addService(this, ms.getFrom()); user =
				 * ms.getFrom(); num = 1; }
				 */
			
				
				if (type.getM_type().equals(MessageType.LOG_IN)) {
					// verify the account name and the password
					boolean in = false;
					String account = ms.getFrom();
					String password = ms.getPassword();
					// Check whether the account has been online or not
					if (accounts.contains(account)
							&& !ManageService.getOnline().contains(account)) {
						if (verify.get(account).equals(password)) {
							in = true;

						}
					}
					if (in) {
						// Check whether the current number of clients online
						// has exceeded the max clients number or not
						if (ManageService.getOnline().size() < MyServer.MAXCLIENTS) {
							ms.setLoginResult(true);
							ManageService.addService(this, ms.getFrom());
							ManageService.addOnline(account);
							this.setUser(ms.getFrom());
							System.out.println(account + " login");
						} else {
							ms.setLoginResult(false);
						}
					} else {
						ms.setLoginResult(false);
					}
					// if the verification succeed, we send to all about this
					// login information
					if (ms.getLoginResult()) {

						for (int i = 0; i < ManageService.getOnline().size(); i++) {
							MyService mservice = ManageService
									.get_Specifc_Service(ManageService
											.getOnline().get(i));
							oos = new ObjectOutputStream(mservice.getSocket()
									.getOutputStream());
							oos.writeObject(ms);
							oos.flush();
						}
					} else {
						// if the verification process failed, send back the
						// information to deny the login
						oos = new ObjectOutputStream(client.getOutputStream());
						oos.writeObject(ms);
						oos.flush();
					}

				} else if (type.getM_type().equals(MessageType.ASK_LIST)) {
					// send the arraylist of account names to the client
					ms.setOnline(ManageService.getOnline());
					oos = new ObjectOutputStream(client.getOutputStream());
					oos.writeObject(ms);
					oos.flush();

				} else if (type.getM_type().equals(MessageType.MESSAGE)) {
					// if this is a message from the client
					String account = ms.getFrom();
					String content = ms.getContent();
					// Check is this message meant to send to all or not
					if (ms.isTo_all() == true) {
						// send to all the current clients online
						System.out.println(account + ": " + content);
						for (int i = 0; i < ManageService.getOnline().size(); i++) {
							if (!ManageService.getOnline().get(i)
									.equals(account)) {
								MyService mservice = ManageService
										.get_Specifc_Service(ManageService
												.getOnline().get(i));
								oos = new ObjectOutputStream(mservice
										.getSocket().getOutputStream());
								oos.writeObject(ms);
								oos.flush();
							}
						}
					} else {
						// send to a particular client
						String account_to = ms.getTo();
						if (ManageService.getOnline().contains(account_to)) {
							System.out.println(account + "(to " + account_to
									+ "): " + content);
							MyService mservice = ManageService
									.get_Specifc_Service(account_to);

							oos = new ObjectOutputStream(mservice.getSocket()
									.getOutputStream());
							oos.writeObject(ms);
							oos.flush();
						} else {
							// If this account is not currently online, just
							// print out cannot reach this account right now
							System.out.println(account_to + " cannot reach");
						}
					}

				} else if (type.getM_type().equals(MessageType.LOG_OUT)) {
					String account = ms.getFrom();
					System.out.println(account + " logout.");
					// Inform all people that the client left the chat room
					if (ManageService.getOnline().size() > 0) {
						for (int i = 0; i < ManageService.getOnline().size(); i++) {

							MyService mservice = ManageService
									.get_Specifc_Service(ManageService
											.getOnline().get(i));
							oos = new ObjectOutputStream(mservice.getSocket()
									.getOutputStream());
							oos.writeObject(ms);
							oos.flush();

						}
					}
					logout = true;

				}

			}
			// delete the service and the account name from the current online
			// arraylist
			ManageService.delete_Specific_Service(user);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
