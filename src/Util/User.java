package Util;

import java.io.Serializable;
/**
 * 
 * @author Simiao Sun
 *
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
		String operation, account;
		int age;
		String password;
		String nick;
		
		String trends;
		String sex;
		
		String time;
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
		
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		
		
	

}
