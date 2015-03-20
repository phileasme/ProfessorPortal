package gui;

import io.CSVTracker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import records.Assessment;
import records.Result;
import records.Student;
import records.StudentRecords;

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

	private StudentRecords studentRecords;
	private CSVTracker tracker;
	
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
		this.studentRecords = studentRecords;
		
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		// attempt to stop swing bug to do with repainting tabs
		setRequestFocusEnabled(false);
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
		Map<String,Assessment>  assess = (HashMap<String,Assessment>) assessments;
		
		List<Assessment> assessmentList = new ArrayList<Assessment>( assess.values());
				
		String[] columns = {"Student Name", "Student Number", "Mark", "Grade"};
		
		for (Assessment a : assessmentList) {
			// will be, e.g. "4CCS1PRA-001"
			String tabName = a.toString();
			
			// table already exists; skip it
			if (indexOfTab(tabName) >= 0) continue;
			
			// one row for each result
			Object[][] results = new Object[a.size()][4];
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
				
				results[rowIndex][0] = studentName;
				results[rowIndex][1] = studentNumber;
				results[rowIndex][2] = r.mark;
				results[rowIndex][3] = r.grade;
			}
			
			/* create the table from the data and give it a mouse listener
			which will make a pop-up window with student info when the name
			is clicked on */
			final JTable table = new JTable(new ResultsTableModel(results, columns));
			
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (table.getSelectedColumn() == 0) {
						int row = table.getSelectedRow();
						String num = (String) table.getValueAt(row, 1);
						
						if (studentRecords.hasStudent(num)) {
							PopUpWindow p = new PopUpWindow(studentRecords.returnStudent(num));
							p.setVisible(true);
						}
					}
				}
			});
			
//			table.setAutoCreateRowSorter(true);
			table.setCellEditor(null);
			JScrollPane scrollPane = new JScrollPane(table);
			addTab(tabName, scrollPane);
			
			int index = indexOfTab(tabName);
			
			ResultsTabPanel tabPanel = new ResultsTabPanel(tabName);
			tabPanel.addActionListener(new CloseTabListener(tabName));
			
			setTabComponentAt(index, tabPanel);
		}  
	
	}
	
	/**
	 * Searches the currently selected tab and makes a scatterplot where each 
	 * point is a student's mark for the current assessment vs the average 
	 * mark across all <b>other</b> assessments. A data point is made for each 
	 * student in the current assessment.
	 */
	public void plotAverageMarks(){
		int index = getSelectedIndex();
		String title = getTitleAt(index);
		Assessment  ass = studentRecords.getAssessment(title);
		
		Scatterplot st = new Scatterplot(title);
		
		// iterate through all results (rows of table) in current assessment
		for (ListIterator<Result> it = ass.listIterator(); it.hasNext();){
	
			Result r = it.next();
			Student s = studentRecords.returnStudent(r.getCandKey());
			
			Collection<Result> studentResults = s.getAllResults();
			
			double assResult = r.mark;
			double average = 0;
			double resultCount = 0;
	
			// add marks for not-currently-selected assessment
			for (Result result : studentResults) {
				if (!(result == r)) {
					average += result.mark;
					++resultCount;
				}
			}
			
			if (resultCount == 0) {
				average = 0;
			} else {
				average = average / resultCount;
			}
			
		    // populate the scatterplot 
			st.addPoint(average, assResult);
			 
			
//			System.out.println(String.format("%-40s MARK = %2.1f AVERAGE = %2.1f", s.getName(), assResult, average));
		}

		st.launch();
		
	}
	
	private CSVTracker getTracker() {
		return tracker;
	}
	
	/**
	 * Assigns an instance of {@link CSVTracker} to the object, so that the 
	 * closing of tabs can be dealt with effectively.
	 * 
	 * @param tracker an instance of CSVTracker
	 */
	public void setTracker(CSVTracker tracker) {
		this.tracker = tracker;
	}
	
	class CloseTabListener implements ActionListener {
		
		private String tabName;
		
		CloseTabListener(String tabName) {
			this.tabName = tabName;
		}
		
		public void actionPerformed(ActionEvent e) {
			int index = indexOfTab(tabName);
			studentRecords.removeAssessment(tabName);
			CSVTracker t = getTracker();
			t.removeEntry(tabName);
			remove(index);
			
		}
	}
}
