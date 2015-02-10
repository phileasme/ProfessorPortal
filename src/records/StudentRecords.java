package records;

/**
 * @author Phileas Hocquard
 * 
 * This class populates an Arraylist of type Student 
 * so that we can have a collection with all the students
 *  from the data spreadsheet.
 * 
 */

/**
 * Importing libraries and packages
 */
import java.util.Map;
import java.util.LinkedHashMap;

import studentdata.*;
import studentdata.Connector;
import studentdata.DataTable;


/**
 * The Class StudentRecords.
 */
public class StudentRecords {
	
	/**
	 *  Mapping of each student to their ID number.
	 */
	private Map<String, Student> students = new LinkedHashMap<String, Student>();	    
       
	
	/**
     * Instantiates a new student records.
     */
        public StudentRecords(){

        // Create a Connector object and open the connection to the server
		Connector server = new Connector();
        boolean success = server.connect("MNP","0a34d4ea3cc0da36ee91172b9cccb621");
        
        /**
         * In case the servlet fails to connect
         */
        if (success == false) {
            System.out.println("Fatal error: could not open connection to server");
            System.exit(1);
        }
        
        
        DataTable data = server.getData();
        /**
         * Declaring and initialising rowCount
         */
        int rowCount = data.getRowCount();
        
        /**
         *  Looping through all rows and creating an instance for each students 
         *  than adding them to an ArrayList of Students
         */
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
		 * Return size of students.
		 * @return number of students
		 */
		public int numOfStudents(){
			return students.size();
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
