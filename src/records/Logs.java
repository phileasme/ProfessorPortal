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
	 * @param module the module for which we are adding participation data
	 * @param date the last time the module was accessed by the student
	 */
	public static void addToParticipationData(String email, String module, String date){
		if (!participationData.containsKey(email)) {
			participationData.put(email, new TreeMap<String, String>());			
		}

		participationData.get(email).put(module, date);
		System.out.println(participationData.get(email));
	}

	/**
	 * Gets the participation.
	 *
	 * @return the participation
	 */
	public static HashMap<String,TreeMap<String,String>>  getParticipation(){
		return participationData;
	}

	public static TreeMap<String, String> getStudentData(String email) {
		return participationData.get(email);
	}

}
