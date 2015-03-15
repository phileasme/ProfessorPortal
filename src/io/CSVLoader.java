package io;

import javax.swing.JOptionPane;

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
import java.util.regex.Matcher;
import java.util.Observer;
import java.util.Observable;

import records.StudentRecords;
import records.Student;
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
public class CSVLoader implements Observer {

	private StudentRecords studentRecords;
	private Map<String, String> pathMap;
	
	private Pattern angledQuotesPattern;
	private Pattern straightQuotesPattern;
	private Pattern numSymbolPattern;
	private Pattern courseNumPattern;
	private Pattern fileNamePattern;
	
	/**
	 * Constructs a CSVLoader and pre-compiles some patterns to ease string 
	 * replacement later on.
	 * 
	 * @param studentRecords the StudentRecords from the main GUI window
	 */
	public CSVLoader(StudentRecords studentRecords, Map<String, String> pathMap) {
		this.studentRecords = studentRecords;
		this.pathMap = pathMap;
		
		String leftQuote = String.valueOf((char) 8220);
		String rightQuote = String.valueOf((char) 8221);

		// compile these now, **once**, to save CPU time later on
		angledQuotesPattern = Pattern.compile(leftQuote + "|" + rightQuote);
		straightQuotesPattern = Pattern.compile("\"");
		numSymbolPattern = Pattern.compile("#");
		courseNumPattern = Pattern.compile("/\\w$");
		fileNamePattern = Pattern.compile("(\\w+)\\.csv");
	}
	
	/**
	 * Reads from a CSV file, takes the (ID number, marking code) pairs, finds 
	 * the student associated with that number, and stores the marking code in
	 * that Student object inside studentRecords.
	 * 
	 * @param filePath the path to a file containing marking codes
	 * @return false if an error occurs or if the file does not contain marking codes  
	 */
	public boolean loadMarkingCodes(String filePath) {
		Scanner sc = null;
		
		try {
			sc = new Scanner(new BufferedReader(new FileReader(filePath)));
			
//			String[] row = clean(sc.nextLine()).split(",");
			String[] row = sc.nextLine().split(",");
			
			if (row.length == 2) {
				// get filename for dialog message
				Matcher m = fileNamePattern.matcher(filePath);
				String fileName;
				
				if (m.find()) {
					fileName = m.group(1) + " - ";
				} else {
					fileName = "";
				}
				
				// keep track of whether codes have corresponding students on record
				int unknown = 0;
				int known = 0;
				
				// begin by storing first row
				if (studentRecords.hasStudent(row[0])) {
					studentRecords.putCode(row[1], row[0]);
					++known;
				} else {
					++unknown;
				}
				
				// continue with rest of file
				while (sc.hasNextLine()) {
//					row = clean(sc.nextLine()).split(",");
					row = sc.nextLine().split(",");
					if (studentRecords.hasStudent(row[0])) {
						studentRecords.putCode(row[1], row[0]);
						++known;
					} else {
						++unknown;
					}
				}
				
				JOptionPane.showMessageDialog(null, fileName + "Anonymous marking codes imported.\n" + known +
						" codes were for known students; " + unknown + " codes were for unknown students");
			} else {
				showWarning("Incorrect column format in selected CSV file!");
				sc.close();
				return false;
			}
				
		} catch (IOException e) {
			showWarning("Something went wrong!\nCause: " + e.getMessage());
			e.printStackTrace();
		}
		
		if (sc != null) {
			sc.close();
			return true;
		}
		return false;
		
	}

	/**
	 * Loads exam/coursework results into StudentRecords by making Result
	 * objects from each row in the provided CSV.
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
	 * @param filePath the path to a file containing marking codes
	 * @return false if an error occurs or if the file does not contain results
	 */
	public boolean loadExamResults(String filePath) {
		Scanner sc = null;
		
		try {
			sc = new Scanner(new BufferedReader(new FileReader(filePath)));
			
			List<String> categories = Arrays.asList(clean(sc.nextLine().toLowerCase()).split(","));
			
			if (categories.size() >= 5) {
				// first find locations in row of info that we want
				int moduleIndex = categories.indexOf("module");
				int assIndex = categories.indexOf("ass");
				int candKeyIndex = categories.indexOf("cand key");
				int markIndex = categories.indexOf("mark");
				int gradeIndex = categories.indexOf("grade");
				
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
						r = new Result(row[moduleIndex], row[assIndex], id, Integer.parseInt(row[markIndex]), row[gradeIndex]);
						studentRecords.returnStudent(id).addResult(r);
					} else {
						r = new Result(row[moduleIndex], row[assIndex], row[candKeyIndex], Integer.parseInt(row[markIndex]), row[gradeIndex]);
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
							r = new Result(row[moduleIndex], row[assIndex], id, Integer.parseInt(row[markIndex]), row[gradeIndex]);
							studentRecords.returnStudent(id).addResult(r);
						} else {
							r = new Result(row[moduleIndex], row[assIndex], row[candKeyIndex], Integer.parseInt(row[markIndex]), row[gradeIndex]);
						}
						currentAss = r.getAssessment();
						
						if (!assMap.containsKey(currentAss)) {
							assMap.put(currentAss, new Assessment());
						}
						
						assMap.get(currentAss).addResult(r);
					}
					
				} else { // file contains coursework results, with modified ID number
					
					// remove course indicator from end of ID number
					String num = courseNumPattern.matcher(row[candKeyIndex]).replaceFirst("");
					
					r = new Result(row[moduleIndex], row[assIndex], num, Integer.parseInt(row[markIndex]), row[gradeIndex]);
					
					Student student = studentRecords.returnStudent(num);
					if (student != null) student.addResult(r);
					
					currentAss = r.getAssessment();
					
					if (!assMap.containsKey(currentAss)) {
						assMap.put(currentAss, new Assessment());
					}
					
					assMap.get(r.getAssessment()).addResult(r);
					
					// load rest of file
					while (sc.hasNextLine()) {
						row = clean(sc.nextLine()).split(",");
						num = courseNumPattern.matcher(row[candKeyIndex]).replaceFirst("");
						
						r = new Result(row[moduleIndex], row[assIndex], num, Integer.parseInt(row[markIndex]), row[gradeIndex]);
						student = studentRecords.returnStudent(num);
						if (student != null) student.addResult(r);
						
						currentAss = r.getAssessment();
						
						if (!assMap.containsKey(currentAss)) {
							assMap.put(currentAss, new Assessment());
						}
						
						assMap.get(currentAss).addResult(r);
					}
				}
				
				assessments.addAll(assMap.values());
				studentRecords.addAssessments(assessments);
				
				for (String name : assMap.keySet()) {
					pathMap.put(name, filePath);
				}
			} else {
				showWarning("Incorrect column format in selected CSV file!");
				sc.close();
				return false;
			}
			
		} catch (IOException e) {
			showWarning("Something went wrong!\nCause: " + e.getMessage());
			e.printStackTrace();
		}
		
		if (sc != null) {
			sc.close();
			return true;
		}
		return false;
		
		
	}

	/**
	 * Loads a CSV file from a path sent to it by an instance of {@link CSVTracker}.
	 */
	@Override
	public void update(Observable o, Object data) {
		String file = (String) data;
		char type = file.charAt(0);
		
		if (type == CSVTracker.CODES) {
			loadMarkingCodes(file.substring(1, file.length()));
		} else if (type == CSVTracker.RESULTS) {
			loadExamResults(file.substring(1, file.length()));
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
		int charType = Character.getType(s.charAt(0));
		
		if (charType == 29) {
			s = angledQuotesPattern.matcher(s).replaceAll("");
		} else if (charType == 24) {
			s = straightQuotesPattern.matcher(s).replaceAll("");
		}
		
		return numSymbolPattern.matcher(s).replaceAll("");
	}
	
	private void showWarning(String message) {
		JOptionPane.showMessageDialog(null, message, "", JOptionPane.WARNING_MESSAGE);
	}
}
