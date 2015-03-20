package gui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

import records.Student;
import records.StudentRecords;
import io.CSVLoader;
import io.CSVTracker;
import io.Settings;

/**
 * Class to create an interface containing Scrollable list of students
 * Student Search function and Information Pop Up window of each student
 * 
 * @author Phileas Hocquard
 * @author Nikita Vorontsov
 * @author Max Karasinski
 *
 */
public class MainApp extends JFrame {

	private DefaultListModel<Student> studentListModel;
	private JList<Student> studentList;
	private StudentRecords sr = new StudentRecords();
	
	private JPanel studentPanel;
	private JPanel dataPanel;
	private JScrollPane studentScroll;
	private JTextField searchText;
	
	private ResultsTabManager resultTabs;
	private CSVTracker csvTracker;
	private CSVLoader loader;
	private Settings settings;

	private JMenuBar menuBar;
	private JMenuItem loadCodes;
	private JMenuItem loadResults;
	private JMenuItem averageResults;
	private JMenuItem emailToStudents;
	private JMenuItem settingsMenu;
	private JMenuItem fetch;
	
	/**
	 * Constructor for the Interface, setting a size, visibility, exit function and creating the widgets
	 */
	public MainApp(){
		super("PRA Coursework - MNP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,600);
		createMenuBar();
		createStudentList();
		createDataBox();
		settings = new Settings();
		
		// set visible now so that the window is visible when marking codes notification pops up
		setVisible(true);
		
		// creates csv loaders and checks for previously loaded data to load
		createFileLoaders();
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

		//Creates scrollable JList to view students
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
		fetch = new JMenuItem("Fetch Participation");
		settingsMenu = new JMenuItem("Settings");
		
		// want this to remain greyed-out until at least one set of marking codes
		// has been loaded
		loadResults.setEnabled(false);
		
		// want this to remain greyed-out until at least one set of results has
		// been loaded
		averageResults.setEnabled(false);

		CSVLoaderListener CSVll = new CSVLoaderListener();
		loadCodes.addActionListener(CSVll);
		loadResults.addActionListener(CSVll);
		
		// add menus to bar, and items to menus
		menuBar.add(file);
		menuBar.add(data);

		file.add(loadCodes);
		file.add(loadResults);
		data.add(averageResults);
		data.add(emailToStudents);
		data.add(fetch);
		data.add(settingsMenu);
		
		//Opens the Email window
		emailToStudents.addActionListener(new EmailListener());
		
		//Plots average marks of results tabs
		averageResults.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				resultTabs.plotAverageMarks();
			}
		});
		
		settingsMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingsWindow sw = new SettingsWindow(settings);
				sw.setVisible(true);
			}
		});

		//Participant login data
		fetch.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ScraperPopUp scrap = new ScraperPopUp();
			}
		});
	
		setJMenuBar(menuBar);
	}

	private void createDataBox() {
		dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());

		 resultTabs = new ResultsTabManager(this, sr);
			
		dataPanel.add(resultTabs);
		add(dataPanel);	
	}
	
	private void createFileLoaders() {
		// this Map is used to connect Assessment names to the paths
		// of the files they were loaded from. Needed for closing tabs
		Map<String, String> csvPathMap = new HashMap<String, String>();
		
		loader = new CSVLoader(sr, csvPathMap);
		csvTracker = new CSVTracker(csvPathMap);
		csvTracker.addObserver(loader);
		csvTracker.initialise();
		
		if (sr.hasCodes()) {
			loadResults.setEnabled(true);
			if (sr.hasAssessments()) averageResults.setEnabled(true);
		}
		
		resultTabs.setTracker(csvTracker);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				settings.store();
				
				if (Boolean.parseBoolean(settings.get("save_data"))) {
					csvTracker.writeToLog();
				} else {
					csvTracker.flush();
				}
			}
		});
	}

	/**
	 * MouseListener to make a pop up window of the Student Information appear
	 */
	class StudentPressListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e) {
			int index;
			Student stu;
			index = studentList.getSelectedIndex(); //Gets index of selected student
			stu = studentListModel.elementAt(index); //Finds the Student object of the selected student from ListModel
			PopUpWindow studentInfo = new PopUpWindow(stu);
			studentInfo.setVisible(true);
		}
	}

	/**
	 * MouseListener to open the Email Window to email settings
	 */
	class EmailListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			EmailWindow ew = new EmailWindow(sr, settings);
		}
	}
	/**
	 * Listener attached to File -> Load X menu items. Constructs a {@link JFileChooser}
	 * which is restricted to files that have a .csv extension. After selecting a
	 * valid file, a new {@link CSVLoader} is constructed which loads the file.
	 * Multiple file selection is allowed.
	 * <p>
	 * Some code taken/inspired by this 
	 * <a href="http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html">Java tutorial</a>.
	 *
	 */
	class CSVLoaderListener implements ActionListener {

		private String currentPath = null;

		public void actionPerformed(ActionEvent e) {
			JFileChooser fc;
			if (currentPath != null) {
				fc = new JFileChooser(currentPath);
			} else {
				fc = new JFileChooser();
			}

			// make only folders and csv files visible
			fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
			fc.setAcceptAllFileFilterUsed(false);
			fc.setMultiSelectionEnabled(true);

			int returnVal = fc.showOpenDialog(MainApp.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File[] files = fc.getSelectedFiles();

				// will start file chooser in this directory next time
				currentPath = files[0].getAbsolutePath();
				
				// determines whether or not file paths will be logged
				boolean success = false;
				
				String[] paths = new String[files.length];

				for (int i = 0; i < files.length; i++) {
					paths[i] = files[i].getAbsolutePath();
					
					if (e.getSource() == loadCodes) {
						success = loader.loadMarkingCodes(paths[i]);
					} else if (e.getSource() == loadResults) {
						success = loader.loadExamResults(paths[i]);
					}
					
				}
				
				// log file paths to be loaded on program launch
				if (success) {
					if (e.getSource() == loadCodes) {
						csvTracker.addFiles(paths, CSVTracker.CODES);
						if (!loadResults.isEnabled()) loadResults.setEnabled(true);
					} else if (e.getSource() == loadResults) {
						csvTracker.addFiles(paths, CSVTracker.RESULTS);
						if (!averageResults.isEnabled()) averageResults.setEnabled(true);
					}
				}

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
		} else {
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


	public static void main (String[] args) throws Exception {
		MainApp mi = new MainApp();
	}
}
