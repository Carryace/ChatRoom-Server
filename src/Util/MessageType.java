package Util;

import java.io.Serializable;

/**
 * 
 * @author Simiao Sun
 *
 */
public class MessageType implements Serializable {
	/**
	 * define the message type
	 */
	private static final long serialVersionUID = 1L;
	public static final String LOG_IN = "login";
	public static final String MESSAGE = "message";
	public static final String ASK_LIST = "list";
	public static final String LOG_OUT = "logout";

	private String m_type;

	public MessageType(String type) {
		setM_type(type);
	}

	public String getM_type() {
		return m_type;
	}

	public void setM_type(String m_type) {
		this.m_type = m_type;
	}

}
