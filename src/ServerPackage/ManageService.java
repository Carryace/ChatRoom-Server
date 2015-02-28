package ServerPackage;

import java.util.ArrayList;
import java.util.HashMap;
/*
 * author: Simiao Sun 14200883
 * This class is created to manage the service thread on the server
 */

public class ManageService {
	private static HashMap<String, MyService> services = new HashMap<String, MyService>();
	private static ArrayList<String> on_line = new ArrayList<String>();


	//Add a service to the hashmap by the account name
	public static void addService(MyService myService, String account){
		services.put(account, myService);
		
	}
	
	public static void addOnline(String account){
		on_line.add(account);
	}
	
	public static HashMap<String, MyService> getServices(){
		return services;
	}
	
	//find a service thread by the account name
	public static MyService get_Specifc_Service(String account){
		return services.get(account);
	}
	
	//remove the account after the client has logout
	public static void delete_Specific_Service(String account){
		services.remove(account);
		on_line.remove(account);
	}
	
	//show all the online names
	public static ArrayList<String> getOnline(){
		return on_line;
	}
}
