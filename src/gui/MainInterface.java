package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.*;

import java.io.File;

import gui.ResultsTabManager;
import records.Student;
import records.StudentRecords;
import io.CSVLoader;

/**
 * Class to create an interface containing Scrollable list of students
 * Student Search function and Information Pop Up window of each student
 * 
 * @author Phileas Hocquard
 * @author Nikita Vorontsov
 * @author Max Karasinski
 *
 */
public class MainInterface extends JFrame {
	
	private DefaultListModel<Student> studentListModel;
	private JList<Student> studentList;
	private StudentRecords sr = new StudentRecords();
	private JPanel studentPanel;
	private JPanel dataPanel;
	private JScrollPane studentScroll;
	private JTextField searchText;
	
	private JMenuBar menuBar;
	private JMenuItem loadCodes;
	private JMenuItem loadResults;
	private JMenuItem averageResults;
	private JMenuItem emailToStudents;
	private JMenuItem emailSettings;
	
	/**
	 * Constructor for the Interface, setting a size, visibility, exit function and creating the widgets
	 * 
	 */
	public MainInterface(){
		super("PRA Coursework - MNP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,600);
		
		createMenuBar();
		createStudentList();
		createDataBox();
		
		setVisible(true);
	}
	
	/**
	 * Creates the scrollable list of students by name and ID Number from StudentRecords
	 * 
	 */
	private void createStudentList(){
		setLayout(new BorderLayout());
		
		studentPanel = new JPanel();
		studentPanel.setLayout(new BorderLayout());
		searchText = new JTextField(25);
		studentListModel = new DefaultListModel<Student>();

		//Adds all students from StudentRecords into the studentListModel
		for (Student student : sr.returnStudents().values()) {
			studentListModel.addElement(student);
		}

		studentList = new JList<Student>(studentListModel);
		studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentScroll = new JScrollPane(studentList);
		
		studentPanel.add(searchText, BorderLayout.NORTH);
		studentPanel.add(studentScroll);
		
		add(studentPanel, BorderLayout.WEST);
		
		studentList.addMouseListener(new StudentPressListener());

		SearchFilter sfl = new SearchFilter();
	}
	
	/**
	 * Creates the MenuBar - Sets to Frame
	 */
	private void createMenuBar(){
		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu data = new JMenu("Data");
		
		// menu items
		loadCodes = new JMenuItem("Load anonymous marking codes");
		loadResults = new JMenuItem("Load exam results");
		averageResults = new JMenuItem("Compare to Average");
		emailToStudents = new JMenuItem("Email to Students");
		emailSettings = new JMenuItem("Email Settings");
		
		// want this to remain greyed-out until at least one set of marking codes
		// has been loaded
		loadResults.setEnabled(false);
		
		loadCodes.addActionListener(new CSVLoaderListener());
		loadResults.addActionListener(new CSVLoaderListener());
		
		// add menus to bar, and items to menus
		menuBar.add(file);
		menuBar.add(data);
		
		file.add(loadCodes);
		file.add(loadResults);
		data.add(averageResults);
		data.add(emailToStudents);
		data.add(emailSettings);
		
		setJMenuBar(menuBar);
	}
	
	private void createDataBox() {
		dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		
		ResultsTabManager resultTabs = new ResultsTabManager(this, sr);
		
		JLabel l1 = new JLabel("Nothing here");
		JLabel l2 = new JLabel("HAHA! Nothing here either!");
		
		resultTabs.add("Tab 1", l1);
		resultTabs.add("Tab 2", l2);
		
		dataPanel.add(resultTabs);
		add(dataPanel);
	}
		
	/**
	 * MouseListener to make a pop up window of the Student Information appear
	 */
	class StudentPressListener implements MouseListener{
		public void mousePressed(MouseEvent e) {
			int index;
			Student stu;
			index = studentList.getSelectedIndex();
			stu = studentListModel.elementAt(index);
			PopUpWindow studentInfo = new PopUpWindow(stu);
			studentInfo.setVisible(true);
		}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
	}
	
	/**
	 * Listener attached to File -> Load X menu items. Constructs a {@link JFileChooser}
	 * which is restricted to files that have a .csv extension. After selecting a
	 * valid file, a new {@link CSVLoader} is constructed which loads the file.
	 * <p>
	 * Some code taken/inspired by the Java tutorial 
	 * <a href="http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html">here</a>.
	 *  
	 *
	 */
	class CSVLoaderListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			
			// make only folders and csv files visible
			fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
			fc.setAcceptAllFileFilterUsed(false);
			
			int returnVal = fc.showOpenDialog(MainInterface.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				CSVLoader loader = new CSVLoader(sr, file.getAbsolutePath());
				
				if (e.getSource() == loadCodes) {
					loader.readCSV(CSVLoader.MARKING_CODES);
				} else if (e.getSource() == loadResults) {
					loader.readCSV(CSVLoader.RESULTS);
				}
				
				if (!loadResults.isEnabled()) loadResults.setEnabled(true);
			}
		}
	}
	
	/**
	 *SearchFilter keeps track of searchText textArea  by using 
	 *a DocumentListener and updates studentListModel using filteringList().
	 */
	class SearchFilter {

		public SearchFilter(){
			searchText.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					filteringList();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					filteringList();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					filteringList();
				}
			});
		}
	}
   
   /**
    * Method to change studentListModel
    */
   public void filteringList(){
	   if (searchText.getText().equals("")){
		   for (Student student : sr.returnStudents().values()) {
			   studentListModel.addElement(student);
		   }

	   }
	   else {
		   //Compare searchtext Text with student name and student number
		   studentListModel.removeAllElements();
		   for (Student student : sr.returnStudents().values()) {
			   if(( (student.getName()).toLowerCase() ).contains( (searchText.getText()).toLowerCase())
					   || student.getNumber().toString().contains(searchText.getText()) ){
				   studentListModel.addElement(student);
			   }
		   }

	   }


   }

	
   public static void main (String[] args){
	   MainInterface mi = new MainInterface();
   }
}