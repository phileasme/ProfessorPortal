package gui;

import javax.swing.JTabbedPane;
import javax.swing.JTable;

import java.util.Observable;
import java.util.Observer;
import java.util.List;

import records.StudentRecords;
import records.Assessment;

public class ResultsTabManager extends JTabbedPane implements Observer {

	private MainInterface frame;
	
	public ResultsTabManager(MainInterface frame, StudentRecords studentRecords) {
		this.frame = frame;
		studentRecords.addObserver(this);
	}

	@Override
	public void update(Observable o, Object assessments) {
		List<Assessment> assessmentList = (List<Assessment>) assessments;
		System.out.println("Just recieved notification of " + assessmentList.size() + " new assessments");
	}
}
