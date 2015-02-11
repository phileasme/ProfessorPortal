package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
//import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

import records.StudentRecords;
import records.Result;
import records.Assessment;

/**
 * An object which serves two functions: 1) to load anonymous marking codes and
 * apply them to students that are stored in the StudentRecords, and 2) load exam
 * results and make them available to the main GUI window.
 * 
 * @author Max Karasinski
 *
 */
public class CSVLoader {

	private StudentRecords studentRecords;
	
	/**
	 * Constructs a CSVLoader which will load either anonymous marking codes or
	 * exam results, depending on the length of the header row.
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
			
			List<String> categories = Arrays.asList(clean(sc.nextLine()).split(","));
			
			if (categories.size() == 2) {
				// file contains anonymous marking codes
				loadMarkingCodes(categories, sc);
			} else {
				// file contains exam/CW results
				loadExamResults(categories, sc);
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
	private void loadMarkingCodes(List<String> firstRow, Scanner sc) {
		
		// keep track of whether codes have corresponding students on record
		int loaded = 0;
		int unknown = 0;
		
		// begin by storing first row. Need try/catch in case
		// it's an unrecognised student
		try {
			studentRecords.returnStudent(firstRow.get(0)).setMarkingCode(firstRow.get(1));
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
	 * poop
	 * 
	 * @param categories the header row of the CSV file
	 * @param sc a Scanner that has read the first line of the loaded CSV file
	 */
	private void loadExamResults(List<String> categories, Scanner sc) {
		// first find locations in row of info that we want
		int moduleIndex = categories.indexOf("Module");
		int assIndex = categories.indexOf("Ass");
		int candKeyIndex = categories.indexOf("Cand Key");
		int markIndex = categories.indexOf("Mark");
		int gradeIndex = categories.indexOf("Grade");

		List<Result> results = new ArrayList<Result>();
		
		// determine if we are dealing with exam results or coursework results
		String[] row = clean(sc.nextLine()).split(",");
		boolean exam = Pattern.matches("^[a-zA-Z]\\w+", row[candKeyIndex]);
		
		Map<String, Assessment> assMap = new HashMap<String, Assessment>();
		
		if (exam) {
			// file contains exam results, with anonymous marking codes
			Result r = new Result(row[moduleIndex], row[assIndex], row[candKeyIndex], row[markIndex], row[gradeIndex]);
			
			while (sc.hasNextLine()) {
				row = clean(sc.nextLine()).split(",");
				
			}
			
		} else { // file contains coursework results, with modified ID number
			
			// remove course indicator from end of ID number
			String num = row[candKeyIndex].replaceFirst("/\\w$", "");
			Result r = new Result(row[moduleIndex], row[assIndex], num, row[markIndex], row[gradeIndex]);
		}
		
	}
	
	/**
	 * Removes any form of double quotation marks and number signs ("#") from
	 * the supplied string. It accounts for the quotation marks put in by many
	 * popular spreadsheet applications and text editors.
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

		// remove # now to make life easier later
		return s.replaceAll("#", "");
	}
}