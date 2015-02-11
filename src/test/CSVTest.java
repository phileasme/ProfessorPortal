package test;

import java.util.Map;

import records.StudentRecords;
import io.CSVLoader;


public class CSVTest {
	
	// change paths and stuff as you wish
	
	public static void main(String[] args) {
		String os = System.getProperty("os.name").toLowerCase();
		String root = System.getProperty("user.dir");
		
		String codes = null;
		String results1 = null;
		
		if (os.equals("linux")) {
			codes = root + "/csv/anoncodes1.csv";
			
			results1 = root + "/csv/201314/4CCS1CS1.csv";
		} else if (os.equals("windows")) {
			codes = root + "\\csv\\anoncodes1.csv";
			
			results1 = root + "\\csv\\201314\\4CCS1CS1.csv";
		}
		
		StudentRecords sr = new StudentRecords();
		CSVLoader csv = new CSVLoader(sr, codes);
		csv = new CSVLoader(sr, results1);
		
	}
}
