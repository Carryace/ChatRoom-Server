package Util;

import java.io.Serializable;

/**
 * 
 * @author Simiao Sun
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String operation;
	private String account;
	private String password;
	private String nick;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
}
