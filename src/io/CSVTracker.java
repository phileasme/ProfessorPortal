package io;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Observable;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

/**
 * Creates and tracks a text file in which paths of CSV files that have been 
 * loaded to CSVLoader are stored. Along with the paths themselves, it records 
 * a character to indicate what kind of data the indicated file holds 
 * (i.e. marking codes of exam results). When the main program is loaded, this 
 * class is used to automatically populate the tables of exam results with any 
 * files being tracked.
 * <p>
 * Some code taken/inspired by this
 * <a href="http://docs.oracle.com/javase/tutorial/essential/io/file.html#textfiles">Java tutorial</a>.
 * 
 * @author Max Karasinski
 * @see CSVLoader
 * @see Observable
 *
 */
public class CSVTracker extends Observable {

	/**
	 * Indicates that a file contains marking codes.
	 */
	public static final char CODES = 'C';
	
	/**
	 * Indicates that a file contains exam results.
	 */
	public static final char RESULTS = 'R';

	private String root;
	private String slash;
	private String filename;
	
	// <Assessment.toString(), path_to_csv>
	private Map<String, String> pathMap;

	// contains paths to loaded csv files
	private Map<Character, HashSet<String>> loadedFiles = new HashMap<Character, HashSet<String>>();

	private Charset charset = Charset.forName("UTF-8");

	/**
	 * Creates a new instance and does a little bit of internal book-keeping.
	 * 
	 * @param pathMap a mapping from Assessment names to their corresponding file paths
	 */
	public CSVTracker(Map<String, String> pathMap) {
		root = System.getProperty("user.dir");
		slash = System.getProperty("file.separator");

		filename = root + slash + "loaded_results_log.txt";

		loadedFiles.put(CODES, new HashSet<String>());
		loadedFiles.put(RESULTS, new HashSet<String>());
		
		this.pathMap = pathMap;
	}

	/**
	 * Finalises the instantiation process of the class by either creating a
	 * new text file to store file paths in or loading paths from a pre-existing
	 * one. Should be called immediately after adding a {@link CSVLoader} as an
	 * Observer.
	 */
	public void initialise() {
		if (new File(filename).exists()) {
			populateFileList();
			loadFiles();
		}
	}

	/**
	 * Passes all currently stored file paths to CSVLoader, where the file will
	 * be loaded.
	 */
	private void loadFiles() {
		HashSet<String> markingCodes = loadedFiles.get(CODES);
		HashSet<String> examResults = loadedFiles.get(RESULTS);

		for (String path : markingCodes) {
			setChanged();
			notifyObservers(CODES + path);
		}

		for (String path : examResults) {
			setChanged();
			notifyObservers(RESULTS + path);
		}
	}
	
	/**
	 * Adds file paths to a list corresponding to the data contained in the file.
	 * The specified file type must be one of the following:
	 * <ul>
	 * <li>CODES: file contains anonymous marking codes.</li>
	 * <li>RESULTS: file contains exam/coursework results</li>
	 * </ul>
	 * 
	 * @param paths an array of file paths
	 * @param fileType the type of data contained in the file
	 */
	public void addFiles(String[] paths, char fileType) {
		HashSet<String> category = loadedFiles.get(fileType);
		
		for (String path : paths) {
			category.add(path);
		}
	}
	
	/**
	 * Takes a list of file paths and records them in a text file. A character 
	 * is placed before the path to signify whether the file contains marking 
	 * codes or exam results.
	 */
	public void writeToLog() {
		Set<String> markingCodes = loadedFiles.get(CODES);
		Set<String> examResults = loadedFiles.get(RESULTS);
		
		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename), charset);
			
			for (String path : markingCodes) {
				writer.write(CODES + " " + path, 0, path.length() + 2);
				writer.newLine();
			}
			
			for (String path : examResults) {
				writer.write(RESULTS + " " + path, 0, path.length() + 2);
				writer.newLine();
			}
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			showWarning("Could not find file!\n" + filename);
		} catch (IOException e) {
			showWarning("Something went wrong!\nCause: " + e.getMessage());
		}
	}
	
	/**
	 * Removes the path corresponding to the provided assessment name.
	 * 
	 * @param name the string representation of an {@link records.Assessment}
	 */
	public void removeEntry(String name) {
		loadedFiles.get(RESULTS).remove(pathMap.get(name));
	}
	
	/**
	 * Deletes the log file.
	 */
	public void flush() {
		try {
			if (new File(filename).exists()) {
				Files.delete(Paths.get(filename));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads all previously recorded files and separates them based on whether
	 * they contain marking codes or exam results.
	 */
	private void populateFileList() {
		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get(filename), charset);
			String line = null;

			// format of line is "<key> <path>". Key is single character
			while ((line = reader.readLine()) != null) {
				char key = line.charAt(0);
				
				String path = line.substring(2, line.length());

				loadedFiles.get(key).add(path);
			}

			reader.close();

		} catch (IOException e) {
			showWarning("Something went wrong when reading from " + filename + "\nCause: " + e.getMessage());
		}
	}

	private void showWarning(String message) {
		JOptionPane.showMessageDialog(null, message, "", JOptionPane.WARNING_MESSAGE);
	}
}
