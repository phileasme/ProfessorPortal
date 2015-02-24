package test;

import java.util.Map;

import records.StudentRecords;
import io.CSVLoader;

public class CSVTest {
	
	// change paths and stuff as you wish and all the cool things
	
	public static void main(String[] args) {
		String os = System.getProperty("os.name").toLowerCase();
		String root = System.getProperty("user.dir");
		
		String codes1 = null;
		String codes2 = null;
		String results1 = null;
		String results2 = null;
		
		if (os.equals("linux")) {
			codes1 = root + "/csv/anoncodes1.csv";
			codes2 = root + "/csv/anoncodes2.csv";
			
			results1 = root + "/csv/201314/4CCS1CS1.csv";
			results2 = root + "/csv/201314/4CCS1ELA.csv";
		} else if (os.equals("windows 7")) {
			codes1 = ""+root + "\\csv\\anoncodes1.csv";
			codes2 = ""+root + "\\csv\\anoncodes2.csv";
			
			results1 = root + "\\csv\\201314\\4CCS1CS1.csv";
			results2 = root + "\\csv\\201314\\4CCS1ELA.csv";
		}
		
		StudentRecords sr = new StudentRecords();
		CSVLoader csv = new CSVLoader(sr);
		csv.readCSV(codes1, CSVLoader.MARKING_CODES);
		
		csv.readCSV(codes2, CSVLoader.MARKING_CODES);
		
		csv.readCSV(results1, CSVLoader.RESULTS);
		
		csv.readCSV(results2, CSVLoader.RESULTS);
		
		System.out.println("\nAfter loading, studentRecords.assessments has " + sr.numOfAssessments() + " assessments");
		
	}
}
