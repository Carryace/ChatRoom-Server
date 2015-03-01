package ServerPackage;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * author: Simiao Sun
 */

public class ManageService {
	private static HashMap<String, MyService> services = new HashMap<String, MyService>();
	private static ArrayList<String> on_line = new ArrayList<String>();


	/**
	 * Add a service to the hashmap by the account name
	 * @param myService 
	 * @param account
	 */
	public static void addService(MyService myService, String account){
		services.put(account, myService);
		
	}
	
	/**
	 * Add the account to the online users list
	 * @param account
	 */
	public static void addOnline(String account){
		on_line.add(account);
	}
	
	/**
	 * Get all the services that the server currently has connection with
	 * @return hashmap
	 */
	public static HashMap<String, MyService> getServices(){
		return services;
	}
	
	/**
	 * Find a service thread by the account name
	 * @param account
	 * @return a specific MyService
	 */
	public static MyService get_Specifc_Service(String account){
		return services.get(account);
	}
	
	/**
	 * Remove the account after the client has logout
	 * @param account
	 */
	public static void delete_Specific_Service(String account){
		services.remove(account);
		on_line.remove(account);
	}
	
	/**
	 * Show all the online names
	 * @return online names arraylist
	 */
	public static ArrayList<String> getOnline(){
		return on_line;
	}
}
