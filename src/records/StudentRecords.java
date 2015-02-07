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
import java.util.ArrayList;

import studentdata.*;
import studentdata.Connector;
import studentdata.DataTable;


/**
 * The Class StudentRecords.
 */
public class StudentRecords {
	
	/**
	 *  The students ArrayList.
	 */
	private ArrayList<Student> students = new ArrayList<>();	    
        // Create a Connector object and open the connection to the server
       
	
	/**
     * Instantiates a new student records.
     */
        public StudentRecords(){
        	
        	/** 
        	 * Server connection
        	 */
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
        	//
        		int numb= Integer.parseInt(data.getCell(row,0));
        		String temail = data.getCell(row,1);
        		String name = data.getCell(row,2);
        		String email = data.getCell(row,3);
        		Student st = new Student(numb,temail,name,email);
        	
        		students.add(st);
            }
        	
        }
		
		
		/**
		 * Return students.
		 *
		 * @return the array list
		 */
		public ArrayList<Student> returnStudents(){
			return students;
		}
    
		
		
		/**
		 * Prints all the students.
		 */
		public void printStudents() {
			
			for(Student each : students){
				System.out.println(each);
			}
		}
		

	

}
