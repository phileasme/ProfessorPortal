package records;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import studentdata.Connector;
import studentdata.DataTable;

/**
 * This class stores information on students, anonymous marking codes, and exam
 * results grouped by assessment.
 *  
 * @author Phileas Hocquard
 * @see Student
 * @see Assessment
 * 
 */
public class StudentRecords extends Observable {

	private Map<String, Student> students = new LinkedHashMap<String, Student>();	    
	private Map<String, String> markingCodes = new HashMap<String, String>();
	private Map<String,Assessment> assessments = new HashMap<String,Assessment>();

	/**
	 * Loads student information using an external server API.
	 */
	public StudentRecords() {

		// Create a Connector object and open the connection to the server
		Connector server = new Connector();
		boolean success = server.connect("MNP","0a34d4ea3cc0da36ee91172b9cccb621");

		// In case the servlet fails to connect
		if (success == false) {
			System.out.println("Fatal error: could not open connection to server");
			System.exit(1);
		}

		DataTable data = server.getData();

		// Declaring and initialising rowCount
		int rowCount = data.getRowCount();

		// Loop through all rows and create an instance for each student
		// then adding them to an ArrayList of Students
		for (int row = 0; row < rowCount; ++row) {
			String numb = data.getCell(row, 0);
			String temail = data.getCell(row, 1);
			String name = data.getCell(row, 2);
			String email = data.getCell(row, 3);
			Student st = new Student(numb, temail, name, email);

			students.put(numb, st);
		}

	}

	/**
	 * Return students.
	 *
	 * @return the LinkedHashMap containing students
	 */
	public Map<String, Student> returnStudents(){
		return students;
	}

	/**
	 * Return individual student from its (int) ID number.
	 * 
	 * @return individual student
	 */
	public Student returnStudent(int i){
		return students.get(String.valueOf(i));
	}

	/**
	 * Return individual student from its (string) ID number.
	 * 
	 * @return individual student 
	 */
	public Student returnStudent(String i) {
		return students.get(i);
	}

	/**
	 * Says whether or not the provided student ID exists.
	 * 
	 * @param i the student's ID number
	 * @return true if the student ID exists
	 */
	public boolean hasStudent(String i) {
		if (students.containsKey(i)) {
			return true;
		}

		return false;
	}

	/**
	 * Add a new anonymous marking code, ID number pair.
	 * 
	 * @param markingCode an anonymous marking code
	 * @param idNumber the corresponding student's ID number
	 */
	public void putCode(String markingCode, String idNumber) {
		markingCodes.put(markingCode, idNumber);
	}

	/**
	 * Retrieve the ID number to the corresponding marking code.
	 * 
	 * @param markingCode an anonymous marking code
	 * @return the corresponding student's ID number
	 */
	public String getIDFromCode(String markingCode) {
		return markingCodes.get(markingCode);
	}

	public boolean hasCodes() {
		if (markingCodes.size() > 0) return true;
		return false;
	}
	
	public boolean hasAssessments() {
		if (assessments.size() > 0) return true;
		return false;
	}

	/**
	 * Store a list of assessments which have been loaded with results.
	 * 
	 * @param assessmentList a list of assessments
	 */
	public void addAssessments(List<Assessment> assessmentList) {
		for (Assessment a : assessmentList) {
			if (a.size() > 0) {
				assessments.put(a.toString(), a);

			}
		}

		// alert ResultsTabManager so that it can update result tabs
		setChanged();
		notifyObservers(assessments);
	}
	
	public void removeAssessment(String name) {
		assessments.remove(name);
	}

	/**
	 * Return size of students.
	 * 
	 * @return number of students
	 */
	public int numOfStudents(){
		return students.size();
	}

	/**
	 * Returns number of assessments on record.
	 * 
	 * @return size of assessments list
	 */
	public int numOfAssessments() {
		return assessments.size();
	}

	public Assessment getAssessment(String assesment){
		return assessments.get(assesment);
	}
	/**
	 * Prints all the students.
	 */
	public void printStudents() {

		for(Student each : students.values()){
			System.out.println(each);
		}
	}
}
