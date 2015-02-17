package gui;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import records.Assessment;
import records.StudentRecords;
import records.Result;

public class ResultsTabManager extends JTabbedPane implements Observer {

	private MainInterface frame;
	private JLabel initialLabel;
	
	public ResultsTabManager(MainInterface frame, StudentRecords studentRecords) {
		this.frame = frame;
		studentRecords.addObserver(this);
		
	}

	@Override
	public void update(Observable o, Object assessments) {
		List<Assessment> assessmentList = (ArrayList<Assessment>) assessments;
		
		String[] columns = {"Student Number", "Student Name", "Mark", "Grade"};
		
		for (Assessment a : assessmentList) {
			String tabName = a.toString();
			
			if (indexOfTab(tabName) >= 0) continue;
			
			String[][] results = new String[a.size()][4];
			int rowIndex = 0;
			
			for (ListIterator<Result> it = a.listIterator(); it.hasNext(); ++rowIndex) {
				Result r = it.next();
				String studentNumber = r.getCandKey();
				
				results[rowIndex][0] = studentNumber;
				results[rowIndex][1] = "bob jones";
				results[rowIndex][2] = r.mark;
				results[rowIndex][3] = r.grade;
			}
			
			JTable table = new JTable(results, columns);
			JScrollPane scrollPane = new JScrollPane(table);
			addTab(tabName, scrollPane);
		}
	}
}
