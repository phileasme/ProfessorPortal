package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
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
 * @see records.StudentRecords
 * @see records.Assessment
 * @see records.Result
 *
 */
public class CSVLoader {

	private StudentRecords studentRecords;
	private String filePath;
	
	/**
	 * Marking codes will be passed to the loader.
	 */
	public static final int MARKING_CODES = 0;
	
	/**
	 * Exam/coursework results will be passed to the loader.
	 */
	public static final int RESULTS = 1;
	
	/**
	 * Constructs a CSVLoader which will load either anonymous marking codes or
	 * exam results, depending on the length of the header row.
	 * 
	 * @param studentRecords the StudentRecords from the main GUI window
	 * @param filePath the path to the CSV file to be loaded
	 */
	public CSVLoader(StudentRecords studentRecords, String filePath) {
		this.studentRecords = studentRecords;
		this.filePath = filePath;
	}
	
	/**
	 * Creates a {@link Scanner} that reads the first row of the CSV file given
	 * to the constructor. The number of columns in the first row is used as a
	 * weak test that the file contains the appropriate data for the specified
	 * running mode (which determines the next method to use on the rest of the
	 * file).
	 * 
	 * @param runMode integer indicating the type of data contained in the file
	 * 
	 * @throws IllegalArguentException if runMode is not one of MARKING_CODES or
	 * RESULTS.
	 */
	public void readCSV(int runMode) throws IllegalArgumentException{
		Scanner sc = null;
		
		try {
			sc = new Scanner(new BufferedReader(new FileReader(filePath)));
			
			List<String> categories = Arrays.asList(clean(sc.nextLine()).split(","));
			
			if ((runMode == MARKING_CODES) && (categories.size() == 2)) {
				// file contains anonymous marking codes
				loadMarkingCodes(categories, sc);
			} else if ((runMode == RESULTS) && (categories.size() >= 5)) {
				// file contains exam/CW results
				loadExamResults(categories, sc);
			} else {
				throw new IllegalArgumentException("Illegal argument: use one of CSVLoader.MARKING_CODES or RESULTS");
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
		int unknown = 0;
		
		// begin by storing first row
		if (studentRecords.hasStudent(firstRow.get(0))) {
			studentRecords.returnStudent(firstRow.get(0)).setMarkingCode(firstRow.get(1));
			studentRecords.putCode(firstRow.get(1), firstRow.get(0));
		} else {
			++unknown;
		}
		
		// continue with rest of file
		while (sc.hasNextLine()) {
			String[] row = clean(sc.nextLine()).split(",");
			if (studentRecords.hasStudent(row[0])) {
				studentRecords.returnStudent(row[0]).setMarkingCode(row[1]);
				studentRecords.putCode(row[1], row[0]);
			} else {
				++unknown;
			}
		}
				
		// TODO replace this with popup message box
		System.out.println("Anonymous marking codes imported. " + studentRecords.numOfStudents() +
				" codes were for known students; " + unknown + " codes were for unknown students");
	}

	/**
	 * Loads exam/coursework results into StudentRecords by making Result
	 * objects from each row in the CSV.
	 * <p>
	 * Results are de-anonymised as they are made, by either looking up the
	 * marking code in StudentRecords or by removing the "/1" from the end of
	 * the ID number (depending on whether the CSV is for exam or coursework
	 * results).
	 * <p>
	 * Results are stored in Assessment objects, with new ones being made each
	 * time a new module/assessment pair is encountered. At the end, a list of
	 * these assessments is stored in StudentRecords.
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

		List<Assessment> assessments = new ArrayList<Assessment>();
		
		// determine if we are dealing with exam results or coursework results
		String[] row = clean(sc.nextLine()).split(",");
		boolean exam = Pattern.matches("^[a-zA-Z]\\w+", row[candKeyIndex]);
		
		// keep track of different module/assessment pairs. Key is Result.getAssessment().
		Map<String, Assessment> assMap = new HashMap<String, Assessment>();
		String currentAss;
		Result r;
		
		if (exam) { // file contains exam results, with anonymous marking codes
			
			// de-anonymise the marking code
			String id = studentRecords.getIDFromCode(row[candKeyIndex]);
			
			if (id != null) {
				r = new Result(row[moduleIndex], row[assIndex], id, row[markIndex], row[gradeIndex]);
			} else {
				r = new Result(row[moduleIndex], row[assIndex], row[candKeyIndex], row[markIndex], row[gradeIndex]);
			}
			
			currentAss = r.getAssessment();
			
			if (!assMap.containsKey(currentAss)) {
				assMap.put(currentAss, new Assessment());
			}
			
			assMap.get(r.getAssessment()).addResult(r);
			
			// load rest of file
			while (sc.hasNextLine()) {
				row = clean(sc.nextLine()).split(",");
				id = studentRecords.getIDFromCode(row[candKeyIndex]);
				
				if (id != null) {
					r = new Result(row[moduleIndex], row[assIndex], id, row[markIndex], row[gradeIndex]);
				} else {
					r = new Result(row[moduleIndex], row[assIndex], row[candKeyIndex], row[markIndex], row[gradeIndex]);
				}
				currentAss = r.getAssessment();
				
				if (!assMap.containsKey(currentAss)) {
					assMap.put(currentAss, new Assessment());
				}
				
				assMap.get(currentAss).addResult(r);
			}
			
		} else { // file contains coursework results, with modified ID number
			
			// remove course indicator from end of ID number
			String num = row[candKeyIndex].replaceFirst("/\\w$", "");
			
			r = new Result(row[moduleIndex], row[assIndex], num, row[markIndex], row[gradeIndex]);
			currentAss = r.getAssessment();
			
			if (!assMap.containsKey(currentAss)) {
				assMap.put(currentAss, new Assessment());
			}
			
			assMap.get(r.getAssessment()).addResult(r);
			
			// load rest of file
			while (sc.hasNextLine()) {
				row = clean(sc.nextLine()).split(",");
				num = row[candKeyIndex].replaceFirst("/\\w$", "");
				
				r = new Result(row[moduleIndex], row[assIndex], row[candKeyIndex], row[markIndex], row[gradeIndex]);
				currentAss = r.getAssessment();
				
				if (!assMap.containsKey(currentAss)) {
					assMap.put(currentAss, new Assessment());
				}
				
				assMap.get(currentAss).addResult(r);
			}
		}
		
		 // temporary stuff
		int numResults = 0;
		
		for (Assessment a : assMap.values()) {
			numResults += a.size();
		}
		
		System.out.println("Loaded " + numResults + " results, accross " + assMap.size() + " assessment(s)");
		
		assessments.addAll(assMap.values());
		studentRecords.addAssessments(assessments);
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
