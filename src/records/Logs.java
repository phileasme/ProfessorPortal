package records;

import java.util.HashMap;
import java.util.TreeMap;

// TODO: Auto-generated Javadoc
/**
 * The Class Logs.
 */
public class Logs {

	
	/** True if logged in */
	public static boolean loggedin = false;
	
	/** The participation data. */
	private static HashMap<String,TreeMap<String,String>> participationData =   new HashMap<String,TreeMap<String,String>>();
	
	/** The user's password. */
	public static String pass = "";
	
	/** The user's username. */
	public static String user = "";
	
	/**
	 * Adds to participation data.
	 *
	 * @param email the student's email
	 * @param modDate 
	 */
	public static void addToParticipationData(String email,TreeMap<String,String> modDate){
 participationData.put(email,modDate);
	}
	
	/**
	 * Gets the participation.
	 *
	 * @return the participation
	 */
	public static HashMap<String,TreeMap<String,String>>  getParticipation(){
		return participationData;
	}
	
}
