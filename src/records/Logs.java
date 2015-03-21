package records;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * The Class Logs contains the users login informations and holds the 
 * participation data.
 * 
 * @author Phileas Hocquard
 * @author Max Karasinski
 * 
 */
public class Logs {

	/** True if logged in. */
	public static boolean loggedin = false;

	private static HashMap<String,TreeMap<String,String>> participationData = new HashMap<String,TreeMap<String,String>>();

	/** The user's password. */
	public static String pass = "";

	/** The user's k-number. */
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
	}

	/**
	 * Gets the participation.
	 *
	 * @return the participation
	 */
	public static HashMap<String,TreeMap<String,String>>  getParticipation(){
		return participationData;
	}

	/**
	 * Gets (module, last accessed) pairs for a given student.
	 * 
	 * @param email a student's email address
	 * @return the student's participation data
	 */
	public static TreeMap<String, String> getStudentData(String email) {
		return participationData.get(email);
	}

}
