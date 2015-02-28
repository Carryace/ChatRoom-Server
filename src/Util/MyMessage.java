package Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Simiao Sun
 *
 */
public class MyMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	private String from, to;
	private String content;
	private MessageType type;
	private boolean to_all = false;
	private String password;
	private boolean loginResult = false;
	private ArrayList<String> online = new ArrayList<String>();

	public MyMessage(MessageType type) {
		this.type = type;
	}

	public MyMessage(String account) {
		this.from = account;
	}

	public MyMessage(String account_from, String account_to, String content) {
		this.from = account_from;
		this.to = account_to;
		this.content = content;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * @return the to_all
	 */
	public boolean isTo_all() {
		return to_all;
	}

	/**
	 * @param to_all
	 *            the to_all to set
	 */
	public void setTo_all(boolean to_all) {
		this.to_all = to_all;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the loginResult
	 */
	public boolean isLoginResult() {
		return loginResult;
	}

	/**
	 * @param loginResult
	 *            the loginResult to set
	 */
	public void setLoginResult(boolean loginResult) {
		this.loginResult = loginResult;
	}

	/**
	 * @return the online
	 */
	public ArrayList<String> getOnline() {
		return online;
	}

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(ArrayList<String> online) {
		this.online = online;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
