package Util;
import java.io.Serializable;
import java.util.ArrayList;


public class MyMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String account_from, account_to;
	private String content;
	private MessageType type;
	private boolean to_all = false;
	private String password;
	private boolean login_result = false;
	private ArrayList<String> online = new ArrayList<String>();
	
	public MyMessage(MessageType type){
		this.type = type;
	}
	
	public MyMessage(String account){
		this.account_from = account;
	}
	
	
	public MyMessage(String account_from, String account_to, String content){
		this.account_from = account_from;
		this.account_to = account_to;
		this.content = content;
	}
	
	public String getFrom(){
		return account_from;
	}
	
	public String getTo(){
		return account_to;
	}
	
	public String getContent(){
		return content;
	}
	public void setAccount_from(String account_from) {
		this.account_from = account_from;
	}

	public void setAccount_to(String account_to) {
		this.account_to = account_to;
	}

	public void setLogin_result(boolean login_result) {
		this.login_result = login_result;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public MessageType getType() {
		return type;
	}


	public void setType(MessageType type) {
		this.type = type;
	}
	
	public void setLoginResult(boolean result){
		this.login_result = result;
	}
	
	public boolean getLoginResult(){
		return login_result;
	}

	public boolean isTo_all() {
		return to_all;
	}

	public void setTo_all(boolean to_all) {
		this.to_all = to_all;
	}

	public ArrayList<String> getOnline() {
		return online;
	}

	public void setOnline(ArrayList<String> online) {
		this.online = online;
	}
}
