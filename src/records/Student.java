package records;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class to store basic student information, as retrieved from the server via
 * the StudentData jar.
 * 
 * @author Max Karasinski
 */
public class Student {

	private String number;
	private String name;
	private String email;
	private String tutorEmail;
	private String markingCode = null;
	private Map<String, Result> results = new TreeMap<String, Result>();
	
	/**
	 * Constructor.
	 * 
	 * @param num the student's ID number
	 * @param tEmail the student's tutor's email address
	 * @param n the student's name
	 * @param e the student's email address
	 */
	public Student(String num, String tEmail, String n, String e) {
		number = num;
		name = n;
		email = e;
		tutorEmail = tEmail;
	}
	
	/**
	 * @return the number for this student
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * @return the student's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the student's email address
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return the student's tutor's email address
	 */
	public String getTutorEmail() {
		return tutorEmail;
	}
	
	/**
	 * Set the student's marking code.
	 * 
	 * @param code the student's anonymous marking code
	 */
	public void setMarkingCode(String code) {
		markingCode = code;
	}
	
	/**
	 * @return the student's anonymous marking code
	 */
	public String getMarkingCode() {
		System.out.println("poop!");
		return markingCode;
	}
	
	/**
	 * Adds a result to the student's list of results.
	 * 
	 * @param res the result to be added
	 */
	public void addResult(Result res){
		// e.g. 4CCS1PRA-001
		String key = res.module + "-" + res.assessment;
		results.put(key, res);
	}
	
	/**
	 * Use an result's module + assessment code, formatted as "module-assessment",
	 * e.g. "4CCS1ELA-001", to fetch a result belonging to the student.
	 * 
	 * @param key the module + assessment identifier for the result 
	 * (e.g. "4CCS1ELA-001")
	 * @return the corresponding result
	 */
	public Result getResult(String key) {
		return results.get(key);
	}
	
	/**
	 * Get an iterable object with all the results that have been loaded for
	 * the student, ordered by module.
	 * 
	 * @return an iterable object containing all the results
	 */
	public Collection<Result> getAllResults() {
		return results.values();
	}
	
	/**
	 * @return all the info on the student: number, tutor's email, name, email
	 */
	public String toString() {
		return name + " (" + number + ")";
	}
	
}
