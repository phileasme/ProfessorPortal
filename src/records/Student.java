package records;

import java.util.ArrayList;

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
	private ArrayList<Result> results = new ArrayList<Result>();
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
		return markingCode;
	}
	
	public void addResult(Result res){
		results.add(res);
	}
	/**
	 * @return all the info on the student: number, tutor's email, name, email
	 */
	public String toString() {
		return name + " (" + number + ")";
	}
	
}
