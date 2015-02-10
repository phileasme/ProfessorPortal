package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import records.StudentRecords;

/**
 * An object which serves two functions: 1) to load anonymous marking codes and
 * apply them to students that are stored in the StudentRecords, and 2) load exam
 * results and make them available to the main GUI window.
 * 
 * @author Max Karasinski
 *
 */
public class CSVLoader {

	private int moduleIndex;
	private int assIndex;
	private int candKeyIndex;
	private int markIndex;
	private int gradeIndex;
	
	private StudentRecords studentRecords;
	
	/**
	 * Constructs a CSVLoader which will load exam results.
	 * 
	 * @param filePath the path to the CSV file to be loaded
	 */
	public CSVLoader(String filePath) {
		readCSV(filePath);
	}
	
	/**
	 * Constructs a CSVLoader which will load anonymous marking codes.
	 * 
	 * @param sr the StudentRecords from the main GUI window
	 * @param filePath the path to the CSV file to be loaded
	 */
	public CSVLoader(StudentRecords sr, String filePath) {
		studentRecords = sr;
		readCSV(filePath);
	}
	
	private void readCSV(String path) {
		Scanner sc = null;
		
		try {
			sc = new Scanner(new BufferedReader(new FileReader(path)));
			
			String[] categories = clean(sc.nextLine()).split(",");
			
			if (categories.length == 2) {
				// file contains anonymous marking codes
				loadMarkingCodes(categories, sc);
			}
			
		} catch (IOException e) {
			System.out.println("IO Exception");
			System.out.println(e.getStackTrace());
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
	}
	
	/**
	 * Takes the (ID number, marking code) pairs from the loaded CSV file, finds
	 * the student associated with that number, and stores the marking code in
	 * that Student object inside studentRecords.
	 * 
	 * @param firstRow the first row of the loaded CSV file
	 * @param sc a Scanner that has read the first line of the loaded CSV file
	 */
	private void loadMarkingCodes(String[] firstRow, Scanner sc) {
		
		// keep track of whether codes have corresponding students on record
		int loaded = 0;
		int unknown = 0;
		
		// begin by storing first row. Need try/catch in case
		// it's an unrecognised student
		try {
			studentRecords.returnStudent(firstRow[0]).setMarkingCode(firstRow[1]);
			++loaded;
		} catch (NullPointerException e) {
			++unknown;
		}
		
		// continue with rest of file
		while (sc.hasNextLine()) {
			String[] row = clean(sc.nextLine()).split(",");
			try {
				studentRecords.returnStudent(row[0]).setMarkingCode(row[1]);
				++loaded;
			} catch (NullPointerException e) {
				++unknown;
			}
		}
		
		// TODO replace this with popup message box
		System.out.println("Anonymous marking codes imported. " + loaded +
				" codes were for known students; " + unknown + " codes were for unknown students");
	}

	/**
	 * Removes any form of double quotation marks from the supplied string.
	 * It accounts for the quotation marks put in by many popular spreadsheet
	 * applications and simple text editors.
	 * 
	 * @param s the string to be cleaned
	 * @return the cleaned string
	 */
	private String clean(String s) {
		String leftQuote = String.valueOf((char) 8220);
		String rightQuote = String.valueOf((char) 8221);
		String quote = "\"";
		String empty = "";

		int charType = Character.getType(s.toCharArray()[0]);

		if (charType == 29) {
			// angled opening/closing quotation marks
			s = s.replaceAll(leftQuote + "|" + rightQuote, empty);
		} else if (charType == 24) {
			// vertical double quotation marks
			s = s.replaceAll(quote, empty);
		}

		return s;
	}
}
