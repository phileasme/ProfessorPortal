package test;

import java.util.LinkedHashMap;
import java.util.Map;

import records.StudentRecords;
import records.Student;
import io.CSVLoader;

public class TestFile {
	public static void main(String[] args) {	
		String path = "/home/max/Documents/KCL/PRA/Major CW/marking_codes_test.csv";
		StudentRecords sr = new StudentRecords();
		
		try {
			System.out.println(sr.returnStudent(34).getMarkingCode());			
		} catch (NullPointerException e) {
			System.out.println("null");
		}
		
		CSVLoader c = new CSVLoader(sr, path);
		
		System.out.println(sr.returnStudent(34).getMarkingCode());
	}
}
