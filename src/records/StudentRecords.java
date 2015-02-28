package records;

/**
 * @author Phileas Hocquard
 * 
 * This class populates an Arraylist of type Student 
 * so that we can have a collection with all the students
 *  from the data spreadsheet.
 * 
 */

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import studentdata.Connector;
import studentdata.DataTable;


/**
 * The Class StudentRecords.
 */
public class StudentRecords extends Observable {

	private Map<String, Student> students = new LinkedHashMap<String, Student>();	    
	private Map<String, String> markingCodes = new HashMap<String, String>();
	private Map<String,Assessment> assessments = new HashMap<String,Assessment>();
	
	/**
     * Instantiates a new student records.
     */
        public StudentRecords(){

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
		
		/**
		 * Store a list of assessments which have been loaded with results.
		 * 
		 * @param assessmentList a list of assessments
		 */
		public void addAssessments(List<Assessment> assessmentList) {
			for (Assessment ass : assessmentList) {
				if (ass.size() > 0) {
					assessments.put(ass.toString(),ass);

				}
			}
			
			// alert ResultsTabManager so that it can update result tabs
			setChanged();
			notifyObservers(assessments);
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
