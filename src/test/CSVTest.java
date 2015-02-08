package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
//import java.io.FileNotFoundException;
import java.io.IOException;

public class CSVTest {
	public static void main(String[] args) {
		String path1 = "/home/max/Documents/KCL/PRA/Major CW/exam_results_test.csv";
		
		readCSV(path1);
	}
	
	public static String clean(String s) {
		String leftQuote = String.valueOf((char) 8220);
		String rightQuote = String.valueOf((char) 8221);
		String quote = "\"";
		String empty = "";
		
		int charType = Character.getType(s.toCharArray()[0]);
		
		if (charType == 29) {
			s = s.replaceAll(leftQuote + "|" + rightQuote, empty);
		} else if (charType == 24) {
			s = s.replaceAll(quote, empty);
		}
		
		return s;
	}
	
	public static void readCSV(String path) {
		Scanner sc = null;
		
		try {
			sc = new Scanner(new BufferedReader(new FileReader(path)));
			
			String[] categories = clean(sc.nextLine()).split(",");
			
			for (int i = 0; i < categories.length; ++i) {
				System.out.print(String.format("%-15s", categories[i]));
			}
			System.out.print("\n");
			
			while (sc.hasNextLine()) {
				String[] dataRow = clean(sc.nextLine()).split(",");
				
				for (int i = 0; i < dataRow.length; ++i) {
					String s = dataRow[i];
					// why doesn't this work?
					System.out.print(String.format("%-15s", s.replaceAll("^\"|\"$", "")));
				}
				System.out.print("\n");
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
}
