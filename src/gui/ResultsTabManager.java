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

/**
 * An object which observes an instance of {@link StudentRecords} to detect
 * when new assignments have been loaded. When this happens, create a table
 * of results for the assessment and put it into a new tab in the results
 * area of the main GUI window.
 * 
 * @author Max Karasinski
 *
 */
public class ResultsTabManager extends JTabbedPane implements Observer {

	private MainInterface frame;
	private StudentRecords studentRecords;
	private JLabel initialLabel;
	
	/**
	 * Creates a new ResultsTabManager, and gives it references to the main GUI
	 * window (so that it can add to the results area), and an instance of
	 * StudentRecords (so that it can find student names).
	 * 
	 * @param frame the main window of the GUI program
	 * @param studentRecords the StudentRecords from the main GUI window
	 */
	public ResultsTabManager(MainInterface frame, StudentRecords studentRecords) {
		studentRecords.addObserver(this);
		
		this.frame = frame;
		this.studentRecords = studentRecords;
	}

	/**
	 * Looks through the loaded assessments to see which are not already
	 * displayed in the tabbed pane. Those which are not have their results put
	 * into a {@link JTable}, which is made scrollable before being put into a
	 * new tab.
	 * <p>
	 * The tab will be named according to the module and assessment code of the
	 * underlying {@link Assessment}. For example, "4CCS1PRA-001".
	 */
	@Override
	public void update(Observable o, Object assessments) {
		List<Assessment> assessmentList = (ArrayList<Assessment>) assessments;
		
		String[] columns = {"Student Number", "Student Name", "Mark", "Grade"};
		
		for (Assessment a : assessmentList) {
			// will be, e.g. "4CCS1PRA-001"
			String tabName = a.toString();
			
			// table already exists; skip it
			if (indexOfTab(tabName) >= 0) continue;
			
			// one row for each result
			String[][] results = new String[a.size()][4];
			int rowIndex = 0;
			
			for (ListIterator<Result> it = a.listIterator(); it.hasNext(); ++rowIndex) {
				Result r = it.next();
				String studentNumber = r.getCandKey();
				String studentName;
				
				// possibility that marking codes have not been loaded/are missing
				// so need to avoid having a null Student returned from studentRecords
				if (studentRecords.hasStudent(studentNumber)) {
					studentName = studentRecords.returnStudent(studentNumber).getName();
				} else {
					studentName = "Anonymous Student";
				}
				
				results[rowIndex][0] = studentNumber;
				results[rowIndex][1] = studentName;
				results[rowIndex][2] = r.mark;
				results[rowIndex][3] = r.grade;
			}
			
			// add table to new tab
			JTable table = new JTable(results, columns);
			JScrollPane scrollPane = new JScrollPane(table);
			addTab(tabName, scrollPane);
		}
	}
}
